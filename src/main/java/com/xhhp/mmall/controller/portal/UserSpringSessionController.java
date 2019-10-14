package com.xhhp.mmall.controller.portal;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.JsonUtil;
import com.xhhp.mmall.common.ResponseCode;
import com.xhhp.mmall.common.ServerResponse;
import com.xhhp.mmall.pojo.User;
import com.xhhp.mmall.service.IUserSerivce;
import com.xhhp.mmall.util.CookieUtil;
import com.xhhp.mmall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * UserController class
 *
 * @author Flc
 * @date 2019/9/25
 */
@Controller
@RequestMapping("/user/springsession/")
public class UserSpringSessionController {

    @Autowired
    private IUserSerivce iUserSerivce;

    /**
     * 用户登录功能
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse servletResponse) {
        ServerResponse<User> response = iUserSerivce.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(Const.CurrentUser,response.getData());
//            CookieUtil.writeLoginToken(servletResponse, session.getId());
//            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }
        return response;
    }


    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse logOut(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
//        String loginToken = CookieUtil.readLoginToken(request);
//        CookieUtil.delLoginToken(request, response);
//        RedisPoolUtil.del(loginToken);
        session.removeAttribute(Const.CurrentUser);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session,HttpServletRequest request) {
        User user = (User)session.getAttribute(Const.CurrentUser);
        //System.out.println(user);
//        String loginToken = CookieUtil.readLoginToken(request);
//        if (StringUtils.isEmpty(loginToken)) {
//            return ServerResponse.createByERRORMessage("用户未登录，无法获取当前用户的信息");
//        }
//        String userJsonStr = RedisPoolUtil.get(loginToken);
//        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByERRORMessage("用户未登录，无法获取信息");
    }


}