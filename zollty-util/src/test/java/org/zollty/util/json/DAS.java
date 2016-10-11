package org.zollty.util.json;

import java.util.Date;

public class DAS {
    
    public static void main(String[] args) {
        //expected:<"2015-06-18 [09]:48:28"> but was:<"2015-06-18 [17]:48:28">
        System.out.println(new Date(1434620908674L));
    }

}
