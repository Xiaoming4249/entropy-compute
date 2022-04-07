package entropyCompute;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class Sentence {
    private String sentence;
    //词语表格
    private List<String> wordList = new ArrayList<>();
    //词语个数
    private int wordNumber;
    //private static final String[] NEED_DELET_Str= new String[]{"：","“","”","…………","……","、","，","《","》","‘","’","-","（","）","〖","〗","「","」","[","]","…","."};
    private static final HashSet<String> NEED_DELET_SET = new HashSet<>();

    private  static final String NEED_DELET = "！|。|？|　|：|“|”|…………|……|、|，|《|》|‘|’|-|（|）|〖|〗|」|「|\\[|\\]";

    //构造器，参数为表示句子的字符串
    public Sentence(String sentence, int cutMode) {
        this.sentence = sentence;
        setWordList(cutMode);
    }

    public static void initDict(){
        //加载用户词典
        WordDictionary.getInstance().init(Paths.get("conf"));
        //初始化需要删除符号的集合
        for(int i = 0; i != 123; i++){
            NEED_DELET_SET.add(('!'+i) + "");
        }
    }

    private void setWordList(int cutMode){
        if(cutMode == 1){
            for (char word: sentence.toCharArray()) {
                if (NEED_DELET_SET.contains(word)) continue;
                wordList.add(word + "");
                wordNumber++;
            }
        }
        else {
            JiebaSegmenter segmenter = new JiebaSegmenter();
            //使用结巴分词，设置该句子的词语列表
            List<SegToken> tokens = segmenter.process(sentence, JiebaSegmenter.SegMode.SEARCH);

            for (SegToken token : tokens) {
                if (NEED_DELET_SET.contains(token.word)) continue;
                wordList.add(token.word);
                wordNumber++;
            }
        }
    }

    public List<String> getWordList() {
        return wordList;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public String getSentence() {
        return sentence;
    }
}
