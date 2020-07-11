import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.jdesktop.swingx.prompt.PromptSupport;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.WARNING_MESSAGE;

public class PostToGetWindow {
    private JPanel rootPanel;
    private JLabel urlDesc;
    private JTextField inputUrl;
    private JTextArea formatResult;
    private JTextArea inputRequestParams;
    private JPanel buttonLayout;
    private JButton formatParams;
    private JButton btnGetResult;

    public static void main(String[] args) {
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        JFrame frame = new JFrame("销售易专用小工具-author:lsh");
        JPanel rootPanel = new PostToGetWindow().rootPanel;
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation((int) (width - frame.getWidth()) / 2, (int) (height - frame.getHeight()) / 2);
    }

    public PostToGetWindow() {
        PromptSupport.setPrompt("请输入生成get的url,之格式化参数不用填", inputUrl);
        PromptSupport.setPrompt("请输入Charles粘贴过来的参数", inputRequestParams);
        PromptSupport.setPrompt("这里展示格式化后的参数(以JSON形式)或格式化的get请求", formatResult);
        formatParams.addActionListener(mActionListener);
        btnGetResult.addActionListener(mActionListener);
    }

    private ActionListener mActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String requestParams = inputRequestParams.getText().trim();
            String url = inputUrl.getText().trim();
            if (!requestParams.isEmpty()) {
                if (e.getSource() == formatParams) {
                    Map paramsMap = handlerParams(requestParams);
                    Object paramsMapStr = JSONObject.toJSON(paramsMap);
                    String prettyResult = JSON.toJSONString(paramsMapStr, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                            SerializerFeature.WriteDateUseDateFormat);
                    formatResult.setText(prettyResult);
                } else if (e.getSource() == btnGetResult) {
                    if (url.isEmpty()) {
                        JOptionPane.showMessageDialog(rootPanel, "请输入URL", "提示", JOptionPane.ERROR_MESSAGE, null);
                    } else {
                        if (isMatchUrl(url)) {
                            String newUrl = url.replace("?", "") + "?";
                            newUrl += getUrlParamsByMap(handlerParams(requestParams));
                            formatResult.setText(newUrl);
                        } else {
                            JOptionPane.showMessageDialog(rootPanel, "请输入正确的URL", "提示", JOptionPane.ERROR_MESSAGE, null);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(rootPanel, "请输入Charles拷贝过来的参数", "提示", JOptionPane.ERROR_MESSAGE, null);
            }

        }
    };

    private Map handlerParams(String requestParams) {
        HashMap hashMap = new HashMap();
        String[] lineContentArr = requestParams.split(System.lineSeparator());
        for (String lineContent : lineContentArr) {
            String[] oneLineArr = lineContent.split("text/plain; charset=UTF-8");
            if (oneLineArr == null || oneLineArr.length != 2) {
                JOptionPane.showMessageDialog(rootPanel, "从Charles拷贝的参数格式错误", "提示", JOptionPane.ERROR_MESSAGE, null);
            } else {
                String paramKey = oneLineArr[0].trim();
                String paramsValue = oneLineArr[1].trim();
                hashMap.put(paramKey, paramsValue);
            }
        }
        return hashMap;
    }

    /**
     * 将map转换成url
     */
    private String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = org.apache.commons.lang.StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    private boolean isMatchUrl(String url) {
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(url);
        return m.matches();
    }

}
