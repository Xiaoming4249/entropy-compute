package entropyCompute;

import java.util.HashMap;
import java.util.List;

public class Computer {

    private List<Sentence> sentences;
    private HashMap<String, StatisticNumber> OneWordMap = new HashMap<>();
    private HashMap<String, StatisticNumber> TwoWordsMap = new HashMap<>();
    private HashMap<String, StatisticNumber> ThreeWordsMap = new HashMap<>();

    public Computer(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    private void initMaps(){
        //遍历所有句子的词列表，计算词频
        for (Sentence sent: sentences) {
            //计算一元词频数
            for (int i = 0; i < sent.getWordNumber(); i++) {
                String word = sent.getWordList().get(i);
                if (OneWordMap.containsKey(word)) {
                    OneWordMap.get(word).addWordFrequency();
                    if (i + 1 != sent.getWordNumber())
                        OneWordMap.get(word).addPreNumber();
                } else {
                    OneWordMap.put(word, new StatisticNumber());
                    if (i + 1 != sent.getWordNumber())
                        OneWordMap.get(word).addPreNumber();
                }
                StatisticNumber.OneSum++;
            }
        }
        for (Sentence sent: sentences) {
            //计算二元词频数
            for (int i = 0; i < sent.getWordNumber() - 1; i++) {
                String words = sent.getWordList().get(i) + sent.getWordList().get(i + 1);
                if (TwoWordsMap.containsKey(words)) {
                    TwoWordsMap.get(words).addWordFrequency();
                    if (i + 2 != sent.getWordNumber())
                        TwoWordsMap.get(words).addPreNumber();
                } else {
                    TwoWordsMap.put(words, new StatisticNumber());
                    TwoWordsMap.get(words).preWordNumber = OneWordMap.get(sent.getWordList().get(i)).preNumber;
                    if (i + 2 != sent.getWordNumber())
                        TwoWordsMap.get(words).addPreNumber();
                }
                StatisticNumber.TwoSum++;
            }
        }
        for (Sentence sent: sentences) {
            //计算三元词频数
            for (int i = 0; i < sent.getWordNumber()-2; i++) {
                String words = sent.getWordList().get(i) + sent.getWordList().get(i+1) + sent.getWordList().get(i+2);
                if(ThreeWordsMap.containsKey(words)) {
                    ThreeWordsMap.get(words).addWordFrequency();
                }
                else {
                    ThreeWordsMap.put(words, new StatisticNumber());
                    ThreeWordsMap.get(words).preWordNumber = TwoWordsMap.get(sent.getWordList().get(i)
                            + sent.getWordList().get(i + 1)).preNumber;
                }
                StatisticNumber.ThreeSum ++;
            }
        }

        //计算需要的频率
        for (String key: OneWordMap.keySet()) {
            OneWordMap.get(key).computeProbility(1);
        }
        for (String key: TwoWordsMap.keySet()) {
            TwoWordsMap.get(key).computeProbility(2);
        }
        for (String key: ThreeWordsMap.keySet()) {
            ThreeWordsMap.get(key).computeProbility(3);
        }
    }
    public double[] computeEntropy(){
        this.initMaps();
        double[] result = new double[3];
        //计算一元模型信息熵
        for (String key: OneWordMap.keySet()) {
            StatisticNumber num = OneWordMap.get(key);
            result[0] += -num.probility * Math.log(num.probility)/Math.log(2);
        }
        //计算二元模型信息熵
        for (String key: TwoWordsMap.keySet()) {
            StatisticNumber num = TwoWordsMap.get(key);
            result[1] += -num.probility * Math.log(num.condProbility)/Math.log(2);
        }
        //计算三元模型信息熵
        for (String key: ThreeWordsMap.keySet()) {
            StatisticNumber num = ThreeWordsMap.get(key);
            result[2] += -num.probility * Math.log(num.condProbility)/Math.log(2);
        }
        return result;
    }

    public HashMap<String, StatisticNumber> getOneWordMap() {
        return OneWordMap;
    }

    public HashMap<String, StatisticNumber> getTwoWordsMap() {
        return TwoWordsMap;
    }

    public HashMap<String, StatisticNumber> getThreeWordsMap() {
        return ThreeWordsMap;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public static void main(String[] args) {
        Computer aComputer = new Computer(new SourceReader(1).getSentences());
        double[] result = aComputer.computeEntropy();
        System.out.println("------------以汉字划分----------");
        System.out.println("汉字总个数：" + StatisticNumber.OneSum);
        System.out.println("不同汉字个数：" + aComputer.getOneWordMap().size());
        System.out.println("一元模型信息熵为：" + result[0]);
        System.out.println("二元模型信息熵为：" + result[1]);
        System.out.println("三元模型信息熵为：" + result[2]);


        int sum = StatisticNumber.OneSum;

        //总数清零
        StatisticNumber.OneSum = 0;
        StatisticNumber.TwoSum = 0;
        StatisticNumber.ThreeSum = 0;

        Computer bComputer = new Computer(new SourceReader().getSentences());
        double[] bresult = bComputer.computeEntropy();
        System.out.println("------------以词语划分----------");
        System.out.println("词语总个数：" + StatisticNumber.OneSum);
        System.out.println("平均词长：" + (double) sum / StatisticNumber.OneSum);
        System.out.println("不同词语个数：" + bComputer.getOneWordMap().size());
        System.out.println("一元模型信息熵为：" + bresult[0]);
        System.out.println("二元模型信息熵为：" + bresult[1]);
        System.out.println("三元模型信息熵为：" + bresult[2]);
    }
}
