package com.kim.ccujwc.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import com.kim.ccujwc.model.Course;
import com.kim.ccujwc.model.Grade;
import com.kim.ccujwc.model.News;
import com.kim.ccujwc.model.PersonGrade;
import com.kim.ccujwc.model.SchoolCard;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;
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
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                String title = null;
                try {
                    title = linkTag.getAttribute("title");
                } catch (Exception e) {
                    title = "";
                }
                if (title.equals(""))
                    continue;
                Scanner scan = new Scanner(title);
                course = new Course();
                while (scan.hasNext()) {
                    String temp = scan.nextLine();
                    String[] tag = temp.split("：");
                    if (tag[0] != null && tag[0].equals("开课编号"))
                        course.setCourseNum(tag.length > 1 ? tag[1] : "");
                    if (tag[0] != null && tag[0].equals("课程编码"))
                        course.setCourseCode(tag.length > 1 ? tag[1] : "");
                    if (tag[0] != null && tag[0].equals("课程名称"))
                        course.setCourseName(tag.length > 1 ? tag[1] : "");
                    if (tag[0] != null && tag[0].equals("授课教师"))
                        course.setCourseTeacher(tag.length > 1 ? tag[1] : "");
                    if (tag[0] != null && tag[0].equals("开课时间"))
                        course.setCourseStartTime(tag.length > 1 ? tag[1] : "");
                    if (tag[0] != null && tag[0].equals("上课周次")) {
                        if (tag.length > 1) {
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
                    } else {
                        course.setCourseStartWeek("");
                        course.setCourseEndWeek("");
                    }
                    if (tag[0] != null && tag[0].equals("开课地点"))
                        course.setCoursePlace(tag.length > 1 ? tag[1] : "");
                    if (tag[0] != null && tag[0].equals("上课班级"))
                        course.setCourseClass(tag.length > 1 ? tag[1] : "");
                }
                courses.add(course);
            }
            return courses;
        } else {
            throw new IOException("网络连接异常");
        }
    }

    public static SchoolCard getSchoolCard(HttpClient client) throws IOException, ParserException {
        SchoolCard schoolCard;
        GetMethod get = new GetMethod(MyUrl.SCHOOLCARD + App.Account);
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Cookie", App.cookie + App.cookie2));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.setConnectionTimeout(7 * 1000);

        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            schoolCard = new SchoolCard();
            String page = get.getResponseBodyAsString();
            Parser parser = new Parser(page);
            AndFilter filter = new AndFilter(new NodeClassFilter(Span.class), new HasAttributeFilter("id"));
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < nodeList.size(); i++) {
                Span span = (Span) nodeList.elementAt(i);
                String id;
                try {
                    id = span.getAttribute("id");
                } catch (Exception e) {
                    id = "";
                }
                switch (id) {
                    case "lbxsh":
                        schoolCard.setDepartment(span.toPlainTextString());
                        break;
                    case "lbzyh":
                        schoolCard.setMajor(span.toPlainTextString());
                        break;
                    case "lbxz":
                        schoolCard.setLengthOfSchooling(span.toPlainTextString());
                        break;
                    case "lbbh":
                        schoolCard.setClas(span.toPlainTextString());
                        break;
                    case "Lbxh":
                        schoolCard.setStudentID(span.toPlainTextString());
                        break;
                }
            }
            parser = new Parser(page);
            filter = new AndFilter(new NodeClassFilter(SelectTag.class), new HasAttributeFilter("name"));
            nodeList = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < nodeList.size(); i++) {
                SelectTag select = (SelectTag) nodeList.elementAt(i);
                String name;
                try {
                    name = select.getAttribute("name");
                } catch (Exception e) {
                    name = "";
                }
                OptionTag[] optionTags;
                switch (name) {
                    case "dropxb":
                        optionTags = select.getOptionTags();
                        for (OptionTag option : optionTags) {
                            if (option.getAttribute("selected") != null && option.getAttribute("selected").equals("selected")) {
                                schoolCard.setSex(option.toPlainTextString());
                                break;
                            }
                        }
                        break;
                    case "dropmz":
                        optionTags = select.getOptionTags();
                        for (OptionTag option : optionTags) {
                            if (option.getAttribute("selected") != null && option.getAttribute("selected").equals("selected")) {
                                schoolCard.setNation(option.toPlainTextString());
                                break;
                            }
                        }
                        break;
                    case "drophf":
                        optionTags = select.getOptionTags();
                        for (OptionTag option : optionTags) {
                            if (option.getAttribute("selected") != null && option.getAttribute("selected").equals("selected")) {
                                schoolCard.setMarriage(option.toPlainTextString());
                                break;
                            }
                        }
                        break;
                    case "dropjtcs":
                        optionTags = select.getOptionTags();
                        for (OptionTag option : optionTags) {
                            if (option.getAttribute("selected") != null && option.getAttribute("selected").equals("selected")) {
                                schoolCard.setFamily(option.toPlainTextString());
                                break;
                            }
                        }
                        break;
                    case "dropzzmm":
                        optionTags = select.getOptionTags();
                        for (OptionTag option : optionTags) {
                            if (option.getAttribute("selected") != null && option.getAttribute("selected").equals("selected")) {
                                schoolCard.setPoliticalLandscape(option.toPlainTextString());
                                break;
                            }
                        }
                        break;
                    case "dropxqxl":
                        optionTags = select.getOptionTags();
                        for (OptionTag option : optionTags) {
                            if (option.getAttribute("selected") != null && option.getAttribute("selected").equals("selected")) {
                                schoolCard.setPreSchoolEducation(option.toPlainTextString());
                                break;
                            }
                        }
                        break;
                }
            }
            parser = new Parser(page);
            filter = new AndFilter(new NodeClassFilter(InputTag.class), new HasAttributeFilter("name"));
            nodeList = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < nodeList.size(); i++) {
                InputTag input = (InputTag) nodeList.elementAt(i);
                String name;
                try {
                    name = input.getAttribute("name");
                } catch (Exception e) {
                    name = "";
                }
                switch (name) {
                    case "tbxsxm":
                        schoolCard.setFullName(input.getAttribute("value"));
                        break;
                    case "tbcsrq":
                        schoolCard.setDateOfBirth(input.getAttribute("value"));
                        break;
                    case "tbbrlxdh":
                        schoolCard.setPhone(input.getAttribute("value"));
                        break;
                    case "ddlzyfx":
                        schoolCard.setProfessionalDirection(input.getAttribute("value"));
                        break;
                    case "tbjg":
                        schoolCard.setHometown(input.getAttribute("value"));
                        break;
                    case "tbhshdrdt":
                        schoolCard.setJoinLeagueTimeAndPlace(input.getAttribute("value"));
                        break;
                    case "tbxxxs":
                        schoolCard.setLearningForm(input.getAttribute("value"));
                        break;
                    case "tbpycc":
                        schoolCard.setLearningLevel(input.getAttribute("value"));
                        break;
                    case "tbzxwyyz":
                        schoolCard.setForeignLanguageTypes(input.getAttribute("value"));
                        break;
                    case "tbrxqgzdw":
                        schoolCard.setPreWorkUnit(input.getAttribute("value"));
                        break;
                    case "tbrxqgzzw":
                        schoolCard.setPosition(input.getAttribute("value"));
                        break;
                    case "tbjtxzdz":
                        schoolCard.setAddress(input.getAttribute("value"));
                        break;
                    case "tbxchcz":
                        schoolCard.setStationGetOff(input.getAttribute("value"));
                        break;
                    case "tbyb":
                        schoolCard.setPostcode(input.getAttribute("value"));
                        break;
                    case "tblxdh":
                        schoolCard.setHomePhone(input.getAttribute("value"));
                        break;
                    case "tblxr":
                        schoolCard.setContacts(input.getAttribute("value"));
                        break;
                    case "tbksh":
                        schoolCard.setEntranceExam(input.getAttribute("value"));
                        break;
                    case "tbsfzh":
                        schoolCard.setIdCardNum(input.getAttribute("value"));
                        break;
                    case "tbxszh":
                        schoolCard.setStudentIDCardNum(input.getAttribute("value"));
                        break;
                    case "tbbjyzsh":
                        schoolCard.setGraduationCertificateNum(input.getAttribute("value"));
                        break;
                    case "tbxszsh":
                        schoolCard.setGraduationCardNum(input.getAttribute("value"));
                        break;
                    case "tbrxrq":
                        schoolCard.setAdmissionDate(input.getAttribute("value"));
                        break;
                    case "tbbyrq":
                        schoolCard.setGraduationDate(input.getAttribute("value"));
                        break;
                }
            }
            return schoolCard;
        } else {
            throw new IOException("网络连接异常");
        }
    }

    public static boolean getStudentImage(HttpClient client, Context context) throws IOException {
        GetMethod get = new GetMethod(MyUrl.STUDENTIMAGE + App.Account + ".jpg");
        client.getHostConfiguration().getParams().setDefaults(new DefaultHttpParams());
        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            InputStream is = get.getResponseBodyAsStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            is.close();
            byte[] data = outputStream.toByteArray();
            File imageFile = new File(context.getFilesDir().getPath(), App.Account + ".jpg");

            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                fileOutputStream.write(data);
            }
            fileOutputStream.close();
            return true;
        } else {
            throw new IOException("网络连接异常");
        }
    }

    public static List<News> getNewsList(HttpClient client) throws IOException, ParserException {
        List<News> newsList = null;
        GetMethod get = new GetMethod(MyUrl.NEWSLIST);
        List<Header> headers = new ArrayList<>();
        headers.add(new Header("Cookie", App.cookie + App.cookie2));
        client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        client.setConnectionTimeout(7 * 1000);

        int statusCode = client.executeMethod(get);
        if (statusCode == 200) {
            newsList = new ArrayList<>();
            String page = get.getResponseBodyAsString();
            Parser parser = new Parser(page);
            AndFilter filter = new AndFilter(new NodeClassFilter(TableTag.class), new HasAttributeFilter("id", "GridView1"));
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < nodeList.size(); i++) {
                TableTag table = (TableTag) nodeList.elementAt(i);
                TableRow[] rows = table.getRows();
                for (int j = 0; j < rows.length; j++) {
                    TableRow row = rows[j];
                    TableColumn[] columns = row.getColumns();
                    News news = new News();
                    if (columns.length > 1) {
                        for (int k = 1; k < columns.length; k++) {
                            TableColumn column = columns[k];
                            NodeList childrens = column.getChildren();
                            Node node = childrens.elementAt(1);
                            if (childrens.size() == 5) {
                                if (node instanceof LinkTag) {
                                    LinkTag link = (LinkTag) node;
                                    String str = link.getAttribute("onclick");
                                    str = str.substring(46, str.length() - 3);
                                    news.setNewsTitle(link.toPlainTextString());
                                    news.setNewsTag(str);
                                }
                            } else {
                                if (node instanceof Span) {
                                    Span span = (Span) node;
                                    String id = span.getAttribute("id");
                                    String[] ids = {"GridView1__ctl" + (j + 2) + "_Label2", "GridView1__ctl" + (j + 2) + "_Label3"};
                                    if (id.equals(ids[0])) {
                                        news.setNewsType(span.toPlainTextString());
                                    }
                                    if (id.equals(ids[1]))
                                        news.setSendTime(span.toPlainTextString());
                                }
                            }
                        }
                    }
                    newsList.add(news);
                }
            }
            newsList.remove(newsList.size() - 1);
            return newsList;
        } else {
            throw new IOException("网络连接异常");
        }
    }
}
