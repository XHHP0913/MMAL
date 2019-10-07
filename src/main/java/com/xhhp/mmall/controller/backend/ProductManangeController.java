package com.xhhp.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.Product;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IFileService;
import com.xhhp.mmall.service.IProductService;
import com.xhhp.mmall.service.IUserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * ProductManangeController class
 *
 * @author Flc
 * @date 2019/9/27
 */
@Controller
@RequestMapping("/manage/product/")
@PropertySource(value = {"classpath:/application-${spring.profiles.active}.properties"})
public class ProductManangeController {

    @Autowired
    private IUserSerivce iUserSerivce;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @Value("${ftp.server.http.prefix}")
    private String ftpPrefix;

    @Value("${ftp.ggg}")
    private String ftp;


    @RequestMapping(value = "kk.do", method = RequestMethod.POST)
    @ResponseBody
    public void kk(HttpSession session, Product product) {
        System.out.println(ftp+"666");
    }


    @RequestMapping(value = "save.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, @RequestParam("productId") Integer productId,@RequestParam("status") Integer status) {
        User user = (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }


    @RequestMapping(value = "detail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, @RequestParam("productId") Integer productId) {
        User user = (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }


    @RequestMapping(value = "list.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.getProductList(pageNum,pageSize);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }

    @RequestMapping(value = "search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,@RequestParam("productName") String productName,@RequestParam("productId") int productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            //增加产品的业务逻辑
            return iProductService.searchProduct(productName,productId,pageNum,pageSize);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }
    }


    @RequestMapping(value = "upload.do")
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request) {

        User user = (User)session.getAttribute(Const.CurrentUser);
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，请登录");
        }
        if(iUserSerivce.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            //String path = PropertiesUtil.getProperty("ftp.server.http.prefix");
            String targetFileName = iFileService.upload(file,path);
            String url = ftpPrefix +"/img/"+ targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);

            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByERRORMessage("无权限操作");
        }




    }





}
