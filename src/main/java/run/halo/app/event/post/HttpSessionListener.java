package run.halo.app.event.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.util.List;
import java.util.Map;

/**
 * 版本        修改时间        作者        修改内容
 * V1.0        2019-11-05        wansu      原始版本
 * 文件说明: HttpSessionListener
 *
 * @description:
 **/
@Component
@Slf4j
public class HttpSessionListener implements javax.servlet.http.HttpSessionListener {
    /**
     * session失效后执行
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        //获取session
        HttpSession session = se.getSession();
        String ip = (String) session.getAttribute("ip");
        //获取servletContext
        ServletContext servletContext = session.getServletContext();
        //获取userMap
        Map<String, List<HttpSession>> userMap = (Map<String, List<HttpSession>>) servletContext.getAttribute("userMap");
        List<HttpSession> sessions = userMap.get(ip);
        log.error("销毁:"+ip);
        //移除session
        sessions.remove(session);
        //如果sessionList的长度为0,说明没有以这个ip访问的用户已经下线,从user,map中移出ip。
        if(sessions.size()==0){
            userMap.remove(ip);
        }else{
            userMap.put(ip, sessions);
        }
        servletContext.setAttribute("userMap", userMap);

    }
}