package com.example.maincode.VO;

import java.util.ArrayList;

public class KakaoVO {
    public ArrayList<Documents> documents;
    public Meta meta;

    public class Documents {
        public String address_name;
        public String category_group_code;
        public String category_group_name;
        public String category_name;
        public String distance;
        public String id;
        public String phone;
        public String place_name;
        public String place_url;
        public String road_address_name;
        public String x;
        public String y;
    }

    public class Meta {
        public String is_end;
        public String pageable_count;
        //        public String same_name;
        public String total_count;
    }
}
