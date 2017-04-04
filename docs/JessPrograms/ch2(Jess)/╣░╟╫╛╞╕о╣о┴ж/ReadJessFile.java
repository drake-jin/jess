import java.io.*;

public class ReadJessFile {
	private String strFilePath = null;
	
	public ReadJessFile(String strFilePath)
	{
		this.strFilePath = strFilePath;
	}
	
	public String getJessFileContent()
	{
		try
		{
		    // 파일로 부터 데이타를 읽어온다(시작) 
			FileInputStream fis = new FileInputStream(this.strFilePath);
			BufferedReader brFileContents = new BufferedReader(new InputStreamReader(fis));
			String strFileContent = "";
			
			String strReturnValue = "";
			
			while(true)
			{				
				strFileContent = brFileContents.readLine();
				
				if(strFileContent == "" || strFileContent == null)
				{
					break;
				}
				else
				{
					strReturnValue += strFileContent + "\n";
				}
			}
			
			return "1" + strReturnValue.trim();
		}
		catch(FileNotFoundException fnfe)
		{
			return "0" + "지정한 파일을 찾을수가 없습니다!!!";
		}
		catch(IOException ioe)
		{
			return "0" + "파일을 읽는 과정에서 에러가 발생하였습니다!!!";
		}
	}
}