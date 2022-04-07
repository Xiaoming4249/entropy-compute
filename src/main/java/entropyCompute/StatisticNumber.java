package entropyCompute;

public class StatisticNumber {

        public static int OneSum = 0;
        public static int TwoSum = 0;
        public static int ThreeSum = 0;
        public int wordFreqency = 1;
        public double probility = 0;
        //计算中要用的条件概率
        public double condProbility = 0;
        //应用于一元和二元组，统计词组中不是尾词的个数
        public int preNumber = 0;
        //应用于二元组和三元组，记录其前一个或两个词与该词组相同的词组的个数
        public int preWordNumber;

        public void addWordFrequency() {
                wordFreqency ++;
        }

        public void addPreNumber() {
                preNumber ++;
        }

        public void computeProbility(int i) {
                switch (i){
                        case 1:
                                this.probility = (double) this.wordFreqency/StatisticNumber.OneSum;
                                break;
                        case 2:
                                this.probility = (double) this.wordFreqency/StatisticNumber.TwoSum;
                                break;
                        case 3:
                                this.probility = (double) this.wordFreqency/StatisticNumber.ThreeSum;
                                break;
                        default:
                                System.out.println("输入参数不正确");
                }
                if(i!=1)this.condProbility = (double) this.wordFreqency/this.preWordNumber;
        }

        @Override
        public String toString() {
                return "词频：" + wordFreqency + ", 非末尾该词数量：" + preNumber +", 前一二词频数：" + preWordNumber;
        }

}

