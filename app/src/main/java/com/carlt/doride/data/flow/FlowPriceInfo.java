package com.carlt.doride.data.flow;

public class FlowPriceInfo {






        public int    id;
        public String name;
        public int    package_month;
        public int    package_size;
        public String price;
        public String packDes;
        public String flowTerm;

        @Override
        public String toString() {
            return "FlowPriceInfo [id=" + id + ", name=" + name
                    + ", package_month=" + package_month + ", package_size="
                    + package_size + ", price=" + price + "]";
        }




}
