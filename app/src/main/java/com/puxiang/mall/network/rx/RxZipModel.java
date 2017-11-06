package com.puxiang.mall.network.rx;

public interface RxZipModel {
    class Model2<M1, M2> {
        private M1 model1;
        private M2 model2;

        public Model2(M1 model1, M2 model2) {
            this.model1 = model1;
            this.model2 = model2;
        }

        public M1 getModel1() {
            return model1;
        }

        public M2 getModel2() {
            return model2;
        }
    }

    class Model3<M1, M2, M3> {
        private M1 model1;
        private M2 model2;
        private M3 model3;

        public Model3(M1 model1, M2 model2, M3 model3) {
            this.model1 = model1;
            this.model2 = model2;
            this.model3 = model3;
        }

        public M1 getModel1() {
            return model1;
        }

        public M2 getModel2() {
            return model2;
        }

        public M3 getModel3() {
            return model3;
        }
    }

    class Model4<M1, M2, M3, M4> {
        private M1 model1;
        private M2 model2;
        private M3 model3;
        private M4 model4;

        public Model4(M1 model1, M2 model2, M3 model3, M4 model4) {
            this.model1 = model1;
            this.model2 = model2;
            this.model3 = model3;
            this.model4 = model4;
        }

        public M1 getModel1() {
            return model1;
        }

        public M2 getModel2() {
            return model2;
        }

        public M3 getModel3() {
            return model3;
        }

        public M4 getModel4() {
            return model4;
        }
    }
}