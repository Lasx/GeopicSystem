package servlets;

import domain.FaceInfo;
import domain.PhotoInfo;
import domain.UserInfo;
import net.sf.json.JSONObject;
import org.opencv.photo.Photo;
import services.FaceService;
import services.PhotoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

@WebServlet(name = "queryServlet")
public class queryServlet extends HttpServlet {
    private UserInfo userInfo;
    private PhotoInfo photoInfo;
    private FaceInfo faceInfo;
    private PhotoService photoService;
    private FaceService faceService;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String userDbname = request.getParameter("userDbname");
        System.out.println(userDbname);
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.fromObject(data);
        System.out.println(jsonObject);
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String address = jsonObject.getString("queryPlace");
        String photoLabels = jsonObject.getString("queryPhotoLabel");
        String queryFaceLabel = jsonObject.getString("queryFaceLabel");
        userInfo = new UserInfo();
        userInfo.setUserDBName("db1");
        faceInfo = new FaceInfo();
        photoInfo = new PhotoInfo();
        faceService = new FaceService();
        photoService = new PhotoService();

        JSONObject res = photoService.getintegratedQueryPhotoPath("2018-07-07 00:00:00","10028-07-07 00:00:00",
                "中南大学", handePhotoLabel(""),handFaceLabel(""),"db1");
        System.out.println(res);
        out.write(res.toString());


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected String handFaceLabel(String faceLabel){
        String facesid="";
        if(faceLabel.indexOf("/")>-1){
            String temp[] = faceLabel.split("/");
            for(int i = 0;i<temp.length;i++){
                faceInfo.setFaceLabel(temp[i]);
                int faceId = faceService.getFaceIdAccordingFaceLabel(faceInfo,userInfo);
                if(i!=(temp.length-1)){
                    facesid += faceId+",";
                }else{
                    facesid += String.valueOf(faceId);
                }
            }

        }else{
            facesid = faceLabel.trim();
        }

        return facesid;
    }

    protected String handePhotoLabel(String photoLabel){
        String photoLabels = "";
        if(photoLabel.indexOf("/")>0){
            String temp[] = photoLabel.split("/");
            for(int i = 0;i<temp.length;i++){
               if(i!=temp.length-1){
                   photoLabels += "\""+temp[i]+"\",";
               }else{
                   photoLabels+="\""+temp[i]+"\"";
               }
            }
        }else{
            photoLabels="\""+photoLabel+"\"";
        }
        return photoLabels;
    }

}
