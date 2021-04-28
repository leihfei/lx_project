package com.lnlr.common.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * @author:leihfei
 * @description 发送邮件工具类
 * @date:Create in 10:18 2018/9/14
 * @email:leihfein@gmail.com
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailUtils {
    /**
     * 收件人邮箱地址
     */
    private String to = "";
    /**
     * 发件人邮箱地址
     */
    private String from = "";
    /**
     * smtp主机
     */
    private String host = "";
    /**
     * 用户名
     */
    private String username = "";
    /**
     * 密码
     */
    private String password = "";
    /**
     * 附件文件名
     */
    private String filename = "";
    /**
     * 邮件主题
     */
    private String subject = "";
    /**
     * 邮件正文
     */
    private String content = "";
    /**
     * 附件文件集合
     */
    private Vector file = new Vector();

    public MailUtils(String to, String from, String smtpServer,
                     String username, String password, String subject, String content) {
        this.to = to;
        this.from = from;
        this.host = smtpServer;
        this.username = username;
        this.password = password;
        this.subject = subject;
        this.content = content;
    }

    /**
     * @param strText
     * @return java.lang.String
     * @author: leihfei
     * @description 把主题转为中文 utf-8
     * @date: 10:23 2018/9/14
     * @email: leihfein@gmail.com
     */
    public String transferChinese(String strText) {
        try {
            strText = MimeUtility.encodeText(new String(strText.getBytes(),
                    "utf-8"), "utf-8", "B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strText;
    }

    public void attachfile(String fname) {
        file.addElement(fname);
    }

    /**
     * @param
     * @return boolean
     * @author: leihfei
     * @description 发送邮件，发送成功返回true 失败false
     * @date: 10:24 2018/9/14
     * @email: leihfein@gmail.com
     */
    public boolean sendMail() {
        // 构造mail session
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.authority", "true");
        props.put("mail.smtp.port", "25");//465
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            // 构造MimeMessage 并设定基本的值
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to));
            subject = transferChinese(subject);
            msg.setSubject(subject);
            // 构造Multipart
            Multipart mp = new MimeMultipart();
            // 向Multipart添加正文
            MimeBodyPart mbpContent = new MimeBodyPart();
            mbpContent.setContent(content, "text/html;charset=utf-8");
            // 向MimeMessage添加（Multipart代表正文）
            mp.addBodyPart(mbpContent);
            // 向Multipart添加附件
            Enumeration efile = file.elements();
            while (efile.hasMoreElements()) {
                MimeBodyPart mbpFile = new MimeBodyPart();
                filename = efile.nextElement().toString();
                FileDataSource fds = new FileDataSource(filename);
                mbpFile.setDataHandler(new DataHandler(fds));
                String filename = new String(fds.getName().getBytes(), "ISO-8859-1");
                mbpFile.setFileName(filename);
                // 向MimeMessage添加（Multipart代表附件）
                mp.addBodyPart(mbpFile);
            }
            file.removeAllElements();
            // 向Multipart添加MimeMessage
            msg.setContent(mp);
            msg.setSentDate(new Date());
            msg.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (Exception mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        MailUtils sendmail = new MailUtils();
        sendmail.setHost("smtp.exmail.qq.com");
        sendmail.setUsername("");
        sendmail.setPassword("");
        sendmail.setTo("");
        sendmail.setFrom("");
        sendmail.setSubject("张小米");
        sendmail.setContent("<a href=\"https://baidu.com\">Link text</a>");
        //添加附件
//      sendmail.attachfile("d:\\CoolFormat3.4.rar");
        System.out.println(sendmail.sendMail());
    }
}
