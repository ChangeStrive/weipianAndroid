package com.platform.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * 发邮件工具类 通过多线程的方式，在后台自动发送邮件
 * @author Saindy Su
 *
 */
public class SendMail implements Runnable {

    private String to; // 接收方

    private String subject; // 标题

    private String content; // 邮件内容

    private Thread thread; // 定义线程

    private String sname; // 邮件署名

    private List attach;//附件

    private String type = "text/html"; // or text/plain 发送邮件的格式

    private String smtpHost; // 邮件服务器 smtp.163.com等
    private String emailFrom; // 发送方邮箱
    private String fromPwd; // 密码

    /**
     * @param to 收信人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param type 发送邮件的格式    "text/html";  or text/plain; 
     */
    public SendMail(String to, String subject, String content, String type) {
        this.sname = sname;
        this.to = to;
        this.subject = subject;
        //attach = new ArrayList();
        this.content = content;
        this.type = type;
        thread = new Thread(this);
        thread.start();
    }

    public boolean send() throws Exception {
        return send(to, subject, content);
    }

    @SuppressWarnings("static-access")
    private boolean send(String to, String subject, String content) {
        boolean blnResult = false;
        InternetAddress[] address = null;
        // mailserver邮件服务器 Form 发送邮件的邮箱 pwd密码
        // 此处三个参数可能通过数据库或.properties等方式来获取，方便后期的管理与设置
        
        //读取配置文件
        PropertiesUtil mPropertiesUnit = new PropertiesUtil();
        Properties mProperties = mPropertiesUnit
                .getProperties("mail.properties");
        
        String mailserver = mProperties.getProperty("apm.mail.smtp.host");
        String sendname = mProperties.getProperty("apm.mail.sendname");// 发送方的邮箱
        String From = mProperties.getProperty("apm.mail.username");// 发送方的邮箱
        String pwd = mProperties.getProperty("apm.mail.logpass");// 密码
        
        From = From + "@"+ mailserver.substring(5);
        
        if (smtpHost != null && !"".equals(smtpHost))
            mailserver = smtpHost;
        if (emailFrom != null && !"".equals(emailFrom))
            From = emailFrom;
        if (fromPwd != null && !"".equals(fromPwd))
            pwd = fromPwd;

        if (smtpHost != null && "nopass".equals(smtpHost)) {
            smtpHost = "";
            System.out.println("发送给" + to + "失败！原因是smtp地址不正确");
            return false;
        }

        String messageText = content;

        boolean sessionDebug = false;
        Date d1 = new Date();
        try {
            // 设定所要用的Mail 服务器和所使用的传输协议
            java.util.Properties props = System.getProperties();
            props.put("mail.host", mailserver);
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");// 指定是否需要SMTP验证

            // 产生新的Session 服务
            javax.mail.Session mailSession = javax.mail.Session
                    .getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            if (sname != null && !"".equals(sname)) {
                // 设定发邮件的人
                msg.setFrom(new InternetAddress(From, sname));
            } else {
                // 带署名的邮件
                msg.setFrom(new InternetAddress(From, ""));
            }

            if (sname != null && "mail".equals(sname)) {
                msg.setFrom(new InternetAddress(From, ""));
            }

            // 设定收信人的信箱
            address = InternetAddress.parse(to, false);
            msg.setRecipients(Message.RecipientType.TO, address);

            // 设定信中的主题
            msg.setSubject(subject);

            // 设定送信的时间
            msg.setSentDate(new Date());

            if ("text/html".equals(this.type)) {
                msg.setContent(messageText, type + ";charset=GB2312");
            } else {
                msg.setText(messageText);
            }

            Transport transport = mailSession.getTransport("smtp");

            transport.connect(mailserver, From, pwd);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            Date d2 = new Date();
            System.out.println("发送给" + to + "成功！耗时"
                    + (d2.getTime() - d1.getTime()) + "毫秒,发送方：" + From);
            blnResult = true;

        } catch (MessagingException mex) {
           // mex.printStackTrace();
            blnResult = false;
        } catch (UnsupportedEncodingException e) {
            //e.printStackTrace();
        }

        return blnResult;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return this.to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void addAttach(String file) {
        attach.add(file);
    }

    public boolean isAttach() {
        return attach != null && attach.size() > 0;
    }

    public static void main(String[] args) {
        try {

            // 普通的文字邮件
            SendMail sendMail = new SendMail("793187559@qq.com", "测试主题",
                    "这是邮件内容哦~~~", "text/plain");
            sendMail.start();
            System.out.println("testing>>>>>>.........");

           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        thread.start();
    }

}
