package edu.k2htm.clientHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;



public class ExecuteRequest {
	/*@Phương thức gửi cho server file và tham số, tham số nào không có thì để null.
	 *  1. firstCoordinate,secondCoordinate: tọa độ 
	 *  2. coment: bình luận
	 *  3. time: thời gian
	 *  4. username: tài khoản gửi yêu cầu
	 *  5. FileLocation: địa chỉ file ảnh
	 *  6. TrafficStatus: tình trạng giao thông
	 */
	public String firstCoord = "" ;
	public String secondCoord = "";
	public String comment = "";
	public String time = "";
	public String username = "";
	public String status ="";
	public String file = "";
	
	public void setTag(String firstCoord, String secondCoord, String comment, String time,
				String username, String status, String file){
		this.firstCoord = firstCoord  ;
		this.secondCoord = secondCoord;
		this.comment = comment;
		this.time = time;
		this.username = username;
		this.status = status;
		this.file = file;
		
	} 
	public String executePost(int firstCoordinate, int secondCoordinate, String comment, long time,
				String username, File file, String TrafficStatus, String StringUrlDestiantion) throws Exception{
		
		HttpPost postRequest = new HttpPost (StringUrlDestiantion) ;
		try{
			
			/*thêm các thành phần, tham số để gửi lên*/
			MultipartEntity multiPartEntity = new MultipartEntity () ;
			multiPartEntity.addPart(this.firstCoord, new StringBody(""+ firstCoordinate));
			multiPartEntity.addPart(this.secondCoord, new StringBody(""+ secondCoordinate));
			multiPartEntity.addPart(this.comment, new StringBody(comment != null ? comment : ""));
			multiPartEntity.addPart(this.time, new StringBody(""+ time));
			multiPartEntity.addPart(this.username, new StringBody(""+ username));
			multiPartEntity.addPart(this.status, new StringBody(""+ TrafficStatus));
			
			FileBody fileBody = new FileBody(file, "application/octect-stream") ;
			/*tham số ảnh*/
			multiPartEntity.addPart(this.file, fileBody) ;
			/*Thêm thực thể vào thông điệp Post*/
			postRequest.setEntity(multiPartEntity);
		}catch (Exception ex){
            ex.printStackTrace() ;
        }
		
		 return execute (postRequest) ;//Xử lí Resquest bình thường
	}
	/* Hàm sử lí các yêu cầu bình thường và xử lí trả về từ server
	 * Mặc định thông điệp trả về là String
	 * */

	public String execute(HttpRequestBase requestBase) throws Exception{
        String responseString = "" ;
        InputStream responseStream = null ;
        HttpClient client = new DefaultHttpClient () ;
            HttpResponse response = client.execute(requestBase) ;
            if (response != null){
                HttpEntity responseEntity = response.getEntity() ;
 
                if (responseEntity != null){
                    responseStream = responseEntity.getContent() ;
                    if (responseStream != null){
                        BufferedReader br = new BufferedReader (new InputStreamReader (responseStream)) ;
                        String responseLine = br.readLine() ;
                        String tempResponseString = "" ;
                        while (responseLine != null){
                            tempResponseString = tempResponseString + responseLine + System.getProperty("line.separator") ;
                            responseLine = br.readLine() ;
                        }
                        br.close() ;
                        if (tempResponseString.length() > 0){
                            responseString = tempResponseString ;
                        }
                    }
                }
            }
        
        client.getConnectionManager().shutdown() ;
        return responseString ;
    }
	
}
