package com.example.baselibs.net.network.bean;

public class Message {
    private String result_num;
    private Data[] result;
    private String log_id;

    public String getResult_num() {
        return result_num;
    }

    public void setResult_num(String result_num) {
        this.result_num = result_num;
    }

    public Data[] getResult() {
        return result;
    }

    public void setResult(Data[] result) {
        this.result = result;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public class Data{
        private String calorie;
        private String has_calorie;
        private String name;
        private String probability;

        public String getCalorie() {
            return calorie;
        }

        public void setCalorie(String calorie) {
            this.calorie = calorie;
        }

        public String getHas_calorie() {
            return has_calorie;
        }

        public void setHas_calorie(String has_calorie) {
            this.has_calorie = has_calorie;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProbability() {
            return probability;
        }

        public void setProbability(String probability) {
            this.probability = probability;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "calorie='" + calorie + '\'' +
                    ", has_calorie='" + has_calorie + '\'' +
                    ", name='" + name + '\'' +
                    ", probability='" + probability + '\'' +
                    '}';
        }
    }
}
