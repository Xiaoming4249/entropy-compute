package entropyCompute;



import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
//资源读取，读取资源目录中的txt文档，并将其按句子转化为字符串数组
public class SourceReader {

    private List<Sentence> sentences = new LinkedList<>();
    //资源文件路径
    private static final String XSDIR =  "Y:\\Code_project\\java\\entropy-compute\\src\\main\\resources\\jinyongxs\\";
    //资源文件目录文件名称
    private  static  final String DIR_FILE = "inf.txt";
    //资源文件名称列表
    private List<String> fileNameList = new ArrayList<>();
    //要删除的标点符号
    private  static final String NEED_DELET = "！|。|？|　|：|“|”|…………|……|、|，|《|》|‘|’|-|（|）|〖|〗|」|「|\\[|\\]";
    //标识句子结尾的符号
    private static final String END_SENTENCE = "！|。|？";

    private int cutMode = 0;

    public SourceReader(){
        setFileNameList();
        //txtFormat();
        setSentences();
    }

    public SourceReader(int cutMode){
        this.cutMode = cutMode;
        setFileNameList();
        //txtFormat();
        setSentences();
    }

    private void setFileNameList(){
        try(
                InputStreamReader dirIs = new InputStreamReader(new FileInputStream(SourceReader.XSDIR + SourceReader.DIR_FILE),
                        "GBK");
        ){
            char[] buffer = new char[500];
            int dLength = dirIs.read(buffer);
            for (String str:new String(buffer, 0 ,dLength).split(",")
                 ) {
                fileNameList.add(str);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setSentences(){
        //加载用户词典
        Sentence.initDict();

        for (String name: fileNameList) {
            try(
                    InputStreamReader is = new InputStreamReader(new FileInputStream(SourceReader.XSDIR + name + "_format.txt"),
                            "UTF-8");
                    BufferedReader TxtIs = new BufferedReader(is);
                    ){
                while(true){
                    String aLine = TxtIs.readLine();
                    if(aLine == null) break;
                    else{
                        sentences.add(new Sentence(aLine, cutMode));
//                        for (String str:
//                             aLine.split(SourceReader.END_SENTENCE)) {
//                            if(str != "")  sentences.add(new Sentence(str, cutMode));
//                        }
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }


    //调整几个文件的格式，去掉不必要的换行和空格
    private void txtFormat(){
        for (String name:
                fileNameList) {
            try(
                    InputStreamReader is = new InputStreamReader(new FileInputStream(SourceReader.XSDIR + name + ".txt"),
                            "GBK");
                    BufferedReader TxtIs = new BufferedReader(is);
                    OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(SourceReader.XSDIR + name + "_format.txt"));
                    BufferedWriter TxtOs = new BufferedWriter(os);
            ){
                while(true){
                    int aByte = TxtIs.read();
                    if(aByte == -1) break;
                    else if(aByte != '\r' && aByte != '\n' && aByte != ' '&& aByte != '　')
                        TxtOs.write(aByte);
                }

            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public static void main(String[] args) {
        Sentence.initDict();
        SourceReader aReader = new SourceReader();
        System.out.println(aReader.getFileNameList());
        try(
                FileOutputStream os = new FileOutputStream(SourceReader.XSDIR +  "allResource.txt");
                PrintStream TxtOs = new PrintStream(os);
                ) {
            for (Sentence sent: aReader.getSentences()) {
                System.out.println(sent.getSentence());
                TxtOs.println(sent.getSentence());
                System.out.println(sent.getWordNumber() + "个词: " + sent.getWordList());
                TxtOs.println(sent.getWordNumber() + "个词: " + sent.getWordList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
