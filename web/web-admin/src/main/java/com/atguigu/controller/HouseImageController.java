package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.HouseImage;
import com.atguigu.result.Result;
import com.atguigu.service.HouseImageService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Date:2022/5/24
 * Author:zh
 * Description:
 */
@Controller
@RequestMapping("/houseImage")
public class HouseImageController {
    private final static String PAGE_UPLOED_SHOW = "house/upload";
    private static final String SHOW_ACTION ="redirect:/house/" ;

    @Reference
    private HouseImageService houseImageService;

    @ResponseBody
    @PostMapping("/upload/{houseId}/{type}")
    public Result upload(@PathVariable("houseId") Long houseId,
                         @PathVariable("type") Integer type,
                         @RequestParam("file") MultipartFile[] multipartFile) throws IOException {

        for (MultipartFile file : multipartFile) {
            String originalFilename = file.getOriginalFilename();
            String uuidName = FileUtil.getUUIDName(originalFilename);
            //上传图片到七牛云
            QiniuUtils.upload2Qiniu(file.getBytes(), uuidName);
            String url = QiniuUtils.getUrl(uuidName);
            HouseImage house1 = new HouseImage();
            house1.setHouseId(houseId);
            house1.setImageUrl(url);
            house1.setImageName(uuidName);
            house1.setType(type);
            houseImageService.insert(house1);
        }
        return Result.ok();
    }

    /**
     * 上传图片,哪个房源,什么图片,拼接在地址后边
     *
     * @param houseId
     * @param type
     * @param model
     * @return
     */
    @GetMapping("/uploadShow/{houseId}/{type}")
    public String uploadShow(@PathVariable("houseId") Long houseId, @PathVariable("type") Integer type, Model model) {
        model.addAttribute("houseId", houseId);
        model.addAttribute("type", type);
        return PAGE_UPLOED_SHOW;
    }

    @GetMapping("delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId")Long houseId,
                         @PathVariable("id")Long id){
                //需要houseId的原因是因为需要重定向需要houseId
        HouseImage houseImage = houseImageService.getById(id);
        QiniuUtils.deleteFileFromQiniu(houseImage.getImageName());
        houseImageService.delete(id);
        return SHOW_ACTION + houseId;
    }

}