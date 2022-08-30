package com.atguigu.controller;

import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.QiniuUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/houseImage")
public class HouseImageController {

    private static final String PAGE_UPLOAD = "house/upload";
    private static final String SHOW_ACTION = "redirect:/house/";

    @Reference
    private HouseImageService houseImageService;
    /**
     * 跳转到house/upload.html页面
     * @param houseId : 房源id
     * @param type : 图片类型
     * @param model
     * @return
     */
    @RequestMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable Long houseId, @PathVariable Integer type, Model model){
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOAD;
    }

    /**
     * 图片上传
     * @param houseId : 房源id
     * @param type : 图片类型
     * @param files : 上传的文件
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable Long houseId, @PathVariable Integer type, @RequestParam("file")MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            //上传文件
            String prefixName = UUID.randomUUID().toString().replace("-", "");
            String suffixName = file.getOriginalFilename().split("\\.")[1];
            String fileName = prefixName + "." + suffixName;
            QiniuUtils.upload2Qiniu(file.getBytes(), fileName);
            //将文件信息保存到数据库
            HouseImage houseImage = new HouseImage();
            houseImage.setHouseId(houseId);
            houseImage.setImageName(fileName);
            houseImage.setImageUrl("http://rh2ng10hp.hn-bkt.clouddn.com/" + fileName);
            houseImage.setType(type);
            houseImageService.insert(houseImage);
        }
        return Result.build(null, ResultCodeEnum.FILE_UPLOAD_SUCCESS);
    }

    /**
     * 删除图片
     * @param houseId : 房源id
     * @param id : 图片id
     * @return
     */
    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable Long houseId, @PathVariable Long id){
        //将图片从七牛云删除
        HouseImage houseImage = houseImageService.getById(id);
        String fileName = houseImage.getImageName();
        QiniuUtils.deleteFileFromQiniu(fileName);
        //将图片从数据库中删除
        houseImageService.delete(id);
        return SHOW_ACTION + houseId;
    }
    
}
