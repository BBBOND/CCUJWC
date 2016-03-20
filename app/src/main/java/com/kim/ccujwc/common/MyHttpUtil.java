package com.kim.ccujwc.common;

import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.Grade;
import com.kim.ccujwc.model.PersonGrade;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by 伟阳 on 2016/3/12.
 */
public class MyHttpUtil {

    private static final String TAG = "MyHttpUtil";

    public static Map<String, String> getParams(HttpClient client) throws IOException, ParserException {
        Map<String, String> map = new HashMap<>();
        client.setConnectionTimeout(10 * 1000);
        GetMethod get = new GetMethod(MyUrl.INDEX);
        client.getHostConfiguration().getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            Header header = get.getResponseHeader("Set-Cookie");
            String cookie = header.getValue().split(";")[0];
            map.put("Cookie", cookie + ";");
            String page = get.getResponseBodyAsString();
            Parser parser = new Parser(page);
            parser.setEncoding("UTF-8");
            NodeFilter inputFilter = new NodeClassFilter(InputTag.class);
            NodeList nodeList = parser.extractAllNodesThatMatch(inputFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.elementAt(i);
                InputTag inputTag = (InputTag) node;
                String key = inputTag.getAttribute("id");
                if (key.equals("__VIEWSTATE") || key.equals("__EVENTVALIDATION")) {
                    map.put(key, inputTag.getAttribute("value"));
                }
            }
            return map;
        } else {
            throw new IllegalStateException("网络连接异常");
        }
    }

    public static boolean login(HttpClient client) throws IOException, ParserException {
        PostMethod post = new PostMethod(MyUrl.LOGIN);
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Cookie", App.cookie));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.setConnectionTimeout(7 * 1000);

        post.addParameter("__VIEWSTATE", App.__VIEWSTATE);
        post.addParameter("__EVENTVALIDATION", App.__EVENTVALIDATION);
        post.addParameter("Account", App.Account);
        post.addParameter("PWD", App.PWD);
        post.addParameter("cmdok", "");

        int statusCode = client.executeMethod(post);
        if (statusCode == 200) {
            return false;
        } else if (statusCode == 302) {
            Header[] h = post.getResponseHeaders();
            String cookie = " ";
            for (Header header : h) {
                if (header.getName().equals("Set-Cookie")) {
                    cookie += header.getValue().split(";")[0];
                    break;
                }
            }
            App.cookie2 = cookie;
            return true;
        } else {
            throw new IOException("ConnectError");
        }
    }

    public static void getName(HttpClient client) throws IOException, ParserException {
        GetMethod get = new GetMethod(MyUrl.DEFAULT);
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Cookie", App.cookie + App.cookie2));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.setConnectionTimeout(7 * 1000);

        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            String page = get.getResponseBodyAsString();
            Parser parser = new Parser(page);
            parser.setEncoding("UTF-8");
            AndFilter andFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("id"));
            NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.elementAt(i);
                if (node.toHtml().contains("users")) {
                    App.Name = node.toPlainTextString().trim();
                    break;
                }
            }
        } else {
            throw new IllegalStateException("网络连接异常");
        }
    }

    public static PersonGrade getGrade(HttpClient client) throws IOException, ParserException {
        PersonGrade personGrade = new PersonGrade();
        List<Grade> gradeList = new ArrayList<>();
        GetMethod get = new GetMethod(MyUrl.GRADE + App.Account);
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Cookie", App.cookie + App.cookie2));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.setConnectionTimeout(7 * 1000);

        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            String page = get.getResponseBodyAsString();
            Parser parser = new Parser(page);

            NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
            NodeList nodeList = parser.extractAllNodesThatMatch(tableFilter);
            for (int i = 1; i < nodeList.size(); i++) {
                Node node = nodeList.elementAt(i);
                String id = "";
                TableTag table = (TableTag) node;
                try {
                    id = table.getAttribute("id").trim();
                } catch (Exception e) {
                    id = "";
                }

                if (id.equals("cjxx")) {
                    TableRow[] row = table.getRows();
                    int j = 0;
                    for (TableRow aRow : row) {
                        Grade grade;
                        if (aRow != null) {
                            TableColumn[] column = aRow.getColumns();
                            if (j != 0) {
                                grade = new Grade();
                                grade.setIsPass(column[0].getStringText());
                                grade.setSemester(column[1].getStringText());
                                grade.setCourseNumber(column[2].getStringText());
                                grade.setCourseName(column[3].getStringText());
                                grade.setSocre(column[4].getStringText());
                                grade.setCredit(column[5].getStringText());
                                grade.setPeriod(column[6].getStringText());
                                grade.setCourseNature(column[7].getStringText());
                                grade.setCourseType(column[8].getStringText());
                                grade.setExamNature(column[9].getStringText());
                                grade.setExamMethod(column[10].getStringText());
                                grade.setMark(column[11].getStringText());
                                grade.setReSemester(column[12].getStringText());
                                gradeList.add(grade);
                            }
                            j++;
                        } else {
                            break;
                        }
                    }
                    personGrade.setGradeList(gradeList);
                }
            }

            Parser parser1 = new Parser(page);
            AndFilter andFilter = new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("id"));
            nodeList = parser1.extractAllNodesThatMatch(andFilter);
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.elementAt(i);
                if (node.toHtml().contains("lbzxf")) {
                    personGrade.setTotalCredits(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lbbxxf")) {
                    personGrade.setCompulsoryCredits(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lblzyxxxf")) {
                    personGrade.setLimitCredits(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lblggxxxf")) {
                    personGrade.setProfessionalElectiveCredits(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("szklb")) {
                    personGrade.setOptionalCredits(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lblpgxfjd")) {
                    personGrade.setGradePointAverage(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lbzxs")) {
                    personGrade.setTotalStudyHours(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lbkcms")) {
                    personGrade.setTotalCoursesNumber(node.toPlainTextString().trim());
                }
                if (node.toHtml().contains("lbbjgms")) {
                    personGrade.setFailedCoursesNumber(node.toPlainTextString().trim());
                }
            }
            return personGrade;
        } else {
            throw new IOException("网络连接异常");
        }
    }

    public static List<Course> getCourses(HttpClient client, String date) throws IOException, ParserException {
        List<Course> courses = new ArrayList<>();
        GetMethod get = new GetMethod(MyUrl.COURSE + "xnxqh=" + date + "&sffd=0");
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Cookie", App.cookie + App.cookie2));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.setConnectionTimeout(7 * 1000);

        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            String page = get.getResponseBodyAsString();
            Parser parser = new Parser(page);
            AndFilter filter = new AndFilter(new NodeClassFilter(LinkTag.class), new HasAttributeFilter("href", "#"));
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            Course course = null;
            for (int i = 1; i < nodeList.size(); i++) {
                LinkTag linkTag = (LinkTag) nodeList.elementAt(i);
                String title = linkTag.getAttribute("title");
                Scanner scan = new Scanner(title);
                course = new Course();
                while (scan.hasNext()) {
                    String temp = scan.nextLine();
                    String[] tag = temp.split("：");
                    if (tag[0] != null && tag[0].equals("开课编号"))
                        course.setCourseNum(tag[1]);
                    if (tag[0] != null && tag[0].equals("课程编码"))
                        course.setCourseCode(tag[1]);
                    if (tag[0] != null && tag[0].equals("课程名称"))
                        course.setCourseName(tag[1]);
                    if (tag[0] != null && tag[0].equals("授课教师"))
                        course.setCourseTeacher(tag[1]);
                    if (tag[0] != null && tag[0].equals("开课时间"))
                        course.setCourseStartTime(tag[1]);
                    if (tag[0] != null && tag[0].equals("上课周次")) {
                        if (tag[1].split("-").length == 2) {
                            course.setCourseStartWeek(tag[1].split("-")[0]);
                            String t = tag[1].split("-")[1];
                            String end = "";
                            for (int j = 0; j < t.length(); j++) {
                                if (t.charAt(j) >= '0' && t.charAt(j) <= '9')
                                    end += t.charAt(j);
                            }
                            course.setCourseEndWeek(end);
                            if (t.contains("单周"))
                                course.setIsSingleWeek(1);
                            else if (t.contains("双周"))
                                course.setIsSingleWeek(2);
                            else
                                course.setIsSingleWeek(0);
                        } else {
                            String[] t = tag[1].split(",");
                            course.setCourseStartWeek(t[0]);
                            course.setCourseEndWeek(t[t.length - 1]);
                            course.setIsSingleWeek(0);
                        }
                    }
                    if (tag[0] != null && tag[0].equals("开课地点"))
                        course.setCoursePlace(tag[1]);
                    if (tag[0] != null && tag[0].equals("上课班级"))
                        course.setCourseClass(tag[1]);
                }
                courses.add(course);
            }
            return courses;
        } else {
            throw new IOException("网络连接异常");
        }
    }
}
