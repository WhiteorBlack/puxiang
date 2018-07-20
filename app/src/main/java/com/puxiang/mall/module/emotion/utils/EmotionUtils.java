
package com.puxiang.mall.module.emotion.utils;

import android.support.v4.util.ArrayMap;

import com.puxiang.mall.R;

import java.util.Map;


/**
 * @description :表情加载类,可自己添加多种表情，分别建立不同的map存放和不同的标志符即可
 */
public class EmotionUtils {

    /**
     * 表情类型标志符
     */
    public static final int EMOTION_A_TYPE = 0x0000;//经典表情
    public static final int EMOTION_B_TYPE = 0x0001;//猴子表情
    public static final int EMOTION_C_TYPE = 0x0002;//猴子表情
    public static final int EMOTION_D_TYPE = 0x0003;//猴子表情
    public static final int EMOTION_E_TYPE = 0x0004;//猴子表情
    public static final int EMOTION_F_TYPE = 0x0005;//猴子表情
    public static final int EMOTION_G_TYPE = 0x0006;//猴子表情

    public static final int EMOTION_BG_TYPE = 0x0010;//猴子表情
    public static final int EMOTION_CG_TYPE = 0x0011;//猴子表情
    public static final int EMOTION_DG_TYPE = 0x0012;//猴子表情
    public static final int EMOTION_EG_TYPE = 0x0013;//猴子表情
    public static final int EMOTION_GG_TYPE = 0x0014;//猴子表情

    /**
     * B
     * key-表情文字;
     * value-表情图片资源
     */
    public static ArrayMap<String, Integer> EMPTY_MAP;
    public static ArrayMap<String, Integer> EMOTION_CLASSIC_MAP;

    public static ArrayMap<String, Integer> EMOTION_B_MAP;
    public static ArrayMap<String, Integer> EMOTION_BG_MAP;

    public static ArrayMap<String, Integer> EMOTION_C_MAP;
    public static ArrayMap<String, Integer> EMOTION_CG_MAP;

    public static ArrayMap<String, Integer> EMOTION_D_MAP;
    public static ArrayMap<String, Integer> EMOTION_DG_MAP;

    public static ArrayMap<String, Integer> EMOTION_E_MAP;
    public static ArrayMap<String, Integer> EMOTION_EG_MAP;

    public static ArrayMap<String, Integer> EMOTION_F_MAP;

    public static ArrayMap<String, Integer> EMOTION_G_MAP;
    public static ArrayMap<String, Integer> EMOTION_GG_MAP;

    static {

        EMPTY_MAP = new ArrayMap<>();
        EMOTION_CLASSIC_MAP = new ArrayMap<>();
//        setEmptyMap(EMOTION_CLASSIC_MAP, EMOTION_A_TYPE, R.mipmap.a00, 63);

        EMOTION_B_MAP = new ArrayMap<>();
        EMOTION_BG_MAP = new ArrayMap<>();
       /*
        setEmptyMap(EMOTION_B_MAP, EMOTION_B_TYPE, R.mipmap.b00, 50);
        setEmptyMap(EMOTION_BG_MAP, EMOTION_B_TYPE, R.mipmap.b00, 50);*/

        EMOTION_C_MAP = new ArrayMap<>();
        EMOTION_CG_MAP = new ArrayMap<>();
//        setEmptyMap(EMOTION_C_MAP, EMOTION_C_TYPE, R.mipmap.c00, 54);
//        setEmptyMap(EMOTION_CG_MAP, EMOTION_C_TYPE, R.mipmap.c00, 54);

        EMOTION_D_MAP = new ArrayMap<>();
        EMOTION_DG_MAP = new ArrayMap<>();
//        setEmptyMap(EMOTION_D_MAP, EMOTION_D_TYPE, R.mipmap.d00, 45);
//        setEmptyMap(EMOTION_DG_MAP, EMOTION_D_TYPE, R.mipmap.d00, 45);

        EMOTION_E_MAP = new ArrayMap<>();
        EMOTION_EG_MAP = new ArrayMap<>();
//        setEmptyMap(EMOTION_E_MAP, EMOTION_E_TYPE, R.mipmap.e00, 30);
//        setEmptyMap(EMOTION_EG_MAP, EMOTION_E_TYPE, R.mipmap.e00, 30);

        EMOTION_F_MAP = new ArrayMap<>();
        // setEmptyMap(EMOTION_F_MAP, EMOTION_F_TYPE, R.mipmap.f00, 16);

        EMOTION_G_MAP = new ArrayMap<>();
        EMOTION_GG_MAP = new ArrayMap<>();
//        setEmptyMap(EMOTION_G_MAP, EMOTION_G_TYPE, R.mipmap.g00, 31);
//        setEmptyMap(EMOTION_GG_MAP, EMOTION_G_TYPE, R.mipmap.g00, 31);


        EMOTION_CLASSIC_MAP.put("[em_0_0]", R.mipmap.a00);
        EMOTION_CLASSIC_MAP.put("[em_0_1]", R.mipmap.a01);
        EMOTION_CLASSIC_MAP.put("[em_0_2]", R.mipmap.a02);
        EMOTION_CLASSIC_MAP.put("[em_0_3]", R.mipmap.a03);
        EMOTION_CLASSIC_MAP.put("[em_0_4]", R.mipmap.a04);
        EMOTION_CLASSIC_MAP.put("[em_0_5]", R.mipmap.a05);
        EMOTION_CLASSIC_MAP.put("[em_0_6]", R.mipmap.a06);
        EMOTION_CLASSIC_MAP.put("[em_0_7]", R.mipmap.a07);
        EMOTION_CLASSIC_MAP.put("[em_0_8]", R.mipmap.a08);
        EMOTION_CLASSIC_MAP.put("[em_0_9]", R.mipmap.a09);
        EMOTION_CLASSIC_MAP.put("[em_0_10]", R.mipmap.a10);
        EMOTION_CLASSIC_MAP.put("[em_0_11]", R.mipmap.a11);
        EMOTION_CLASSIC_MAP.put("[em_0_12]", R.mipmap.a12);
        EMOTION_CLASSIC_MAP.put("[em_0_13]", R.mipmap.a13);
        EMOTION_CLASSIC_MAP.put("[em_0_14]", R.mipmap.a14);
        EMOTION_CLASSIC_MAP.put("[em_0_15]", R.mipmap.a15);
        EMOTION_CLASSIC_MAP.put("[em_0_16]", R.mipmap.a16);
        EMOTION_CLASSIC_MAP.put("[em_0_17]", R.mipmap.a17);
        EMOTION_CLASSIC_MAP.put("[em_0_18]", R.mipmap.a18);
        EMOTION_CLASSIC_MAP.put("[em_0_19]", R.mipmap.a19);
        EMOTION_CLASSIC_MAP.put("[em_0_20]", R.mipmap.a20);
        EMOTION_CLASSIC_MAP.put("[em_0_21]", R.mipmap.a21);
        EMOTION_CLASSIC_MAP.put("[em_0_22]", R.mipmap.a22);
        EMOTION_CLASSIC_MAP.put("[em_0_23]", R.mipmap.a23);
        EMOTION_CLASSIC_MAP.put("[em_0_24]", R.mipmap.a24);
        EMOTION_CLASSIC_MAP.put("[em_0_25]", R.mipmap.a25);
        EMOTION_CLASSIC_MAP.put("[em_0_26]", R.mipmap.a26);
        EMOTION_CLASSIC_MAP.put("[em_0_27]", R.mipmap.a27);
        EMOTION_CLASSIC_MAP.put("[em_0_28]", R.mipmap.a28);
        EMOTION_CLASSIC_MAP.put("[em_0_29]", R.mipmap.a29);
        EMOTION_CLASSIC_MAP.put("[em_0_30]", R.mipmap.a30);
        EMOTION_CLASSIC_MAP.put("[em_0_31]", R.mipmap.a31);
        EMOTION_CLASSIC_MAP.put("[em_0_32]", R.mipmap.a32);
        EMOTION_CLASSIC_MAP.put("[em_0_33]", R.mipmap.a33);
        EMOTION_CLASSIC_MAP.put("[em_0_34]", R.mipmap.a34);
        EMOTION_CLASSIC_MAP.put("[em_0_35]", R.mipmap.a35);
        EMOTION_CLASSIC_MAP.put("[em_0_36]", R.mipmap.a36);
        EMOTION_CLASSIC_MAP.put("[em_0_37]", R.mipmap.a37);
        EMOTION_CLASSIC_MAP.put("[em_0_38]", R.mipmap.a38);
        EMOTION_CLASSIC_MAP.put("[em_0_39]", R.mipmap.a39);
        EMOTION_CLASSIC_MAP.put("[em_0_40]", R.mipmap.a40);
        EMOTION_CLASSIC_MAP.put("[em_0_41]", R.mipmap.a41);
        EMOTION_CLASSIC_MAP.put("[em_0_42]", R.mipmap.a42);
        EMOTION_CLASSIC_MAP.put("[em_0_43]", R.mipmap.a43);
        EMOTION_CLASSIC_MAP.put("[em_0_44]", R.mipmap.a44);
        EMOTION_CLASSIC_MAP.put("[em_0_45]", R.mipmap.a45);
        EMOTION_CLASSIC_MAP.put("[em_0_46]", R.mipmap.a46);
        EMOTION_CLASSIC_MAP.put("[em_0_47]", R.mipmap.a47);
        EMOTION_CLASSIC_MAP.put("[em_0_48]", R.mipmap.a48);
        EMOTION_CLASSIC_MAP.put("[em_0_49]", R.mipmap.a49);
        EMOTION_CLASSIC_MAP.put("[em_0_50]", R.mipmap.a50);
        EMOTION_CLASSIC_MAP.put("[em_0_51]", R.mipmap.a51);
        EMOTION_CLASSIC_MAP.put("[em_0_52]", R.mipmap.a52);
        EMOTION_CLASSIC_MAP.put("[em_0_53]", R.mipmap.a53);
        EMOTION_CLASSIC_MAP.put("[em_0_54]", R.mipmap.a54);
        EMOTION_CLASSIC_MAP.put("[em_0_55]", R.mipmap.a55);
        EMOTION_CLASSIC_MAP.put("[em_0_56]", R.mipmap.a56);
        EMOTION_CLASSIC_MAP.put("[em_0_57]", R.mipmap.a57);
        EMOTION_CLASSIC_MAP.put("[em_0_58]", R.mipmap.a58);
        EMOTION_CLASSIC_MAP.put("[em_0_59]", R.mipmap.a59);
        EMOTION_CLASSIC_MAP.put("[em_0_60]", R.mipmap.a60);
        EMOTION_CLASSIC_MAP.put("[em_0_61]", R.mipmap.a61);
        EMOTION_CLASSIC_MAP.put("[em_0_62]", R.mipmap.a62);

        EMOTION_B_MAP.put("[em_1_0]", R.mipmap.b00);
        EMOTION_B_MAP.put("[em_1_1]", R.mipmap.b01);
        EMOTION_B_MAP.put("[em_1_2]", R.mipmap.b02);
        EMOTION_B_MAP.put("[em_1_3]", R.mipmap.b03);
        EMOTION_B_MAP.put("[em_1_4]", R.mipmap.b04);
        EMOTION_B_MAP.put("[em_1_5]", R.mipmap.b05);
        EMOTION_B_MAP.put("[em_1_6]", R.mipmap.b06);
        EMOTION_B_MAP.put("[em_1_7]", R.mipmap.b07);
        EMOTION_B_MAP.put("[em_1_8]", R.mipmap.b08);
        EMOTION_B_MAP.put("[em_1_9]", R.mipmap.b09);
        EMOTION_B_MAP.put("[em_1_10]", R.mipmap.b10);
        EMOTION_B_MAP.put("[em_1_11]", R.mipmap.b11);
        EMOTION_B_MAP.put("[em_1_12]", R.mipmap.b12);
        EMOTION_B_MAP.put("[em_1_13]", R.mipmap.b13);
        EMOTION_B_MAP.put("[em_1_14]", R.mipmap.b14);
        EMOTION_B_MAP.put("[em_1_15]", R.mipmap.b15);
        EMOTION_B_MAP.put("[em_1_16]", R.mipmap.b16);
        EMOTION_B_MAP.put("[em_1_17]", R.mipmap.b17);
        EMOTION_B_MAP.put("[em_1_18]", R.mipmap.b18);
        EMOTION_B_MAP.put("[em_1_19]", R.mipmap.b19);
        EMOTION_B_MAP.put("[em_1_20]", R.mipmap.b20);
        EMOTION_B_MAP.put("[em_1_21]", R.mipmap.b21);
        EMOTION_B_MAP.put("[em_1_22]", R.mipmap.b22);
        EMOTION_B_MAP.put("[em_1_23]", R.mipmap.b23);
        EMOTION_B_MAP.put("[em_1_24]", R.mipmap.b24);
        EMOTION_B_MAP.put("[em_1_25]", R.mipmap.b25);
        EMOTION_B_MAP.put("[em_1_26]", R.mipmap.b26);
        EMOTION_B_MAP.put("[em_1_27]", R.mipmap.b27);
        EMOTION_B_MAP.put("[em_1_28]", R.mipmap.b28);
        EMOTION_B_MAP.put("[em_1_29]", R.mipmap.b29);
        EMOTION_B_MAP.put("[em_1_30]", R.mipmap.b30);
        EMOTION_B_MAP.put("[em_1_31]", R.mipmap.b31);
        EMOTION_B_MAP.put("[em_1_32]", R.mipmap.b32);
        EMOTION_B_MAP.put("[em_1_33]", R.mipmap.b33);
        EMOTION_B_MAP.put("[em_1_34]", R.mipmap.b34);
        EMOTION_B_MAP.put("[em_1_35]", R.mipmap.b35);
        EMOTION_B_MAP.put("[em_1_36]", R.mipmap.b36);
        EMOTION_B_MAP.put("[em_1_37]", R.mipmap.b37);
        EMOTION_B_MAP.put("[em_1_38]", R.mipmap.b38);
        EMOTION_B_MAP.put("[em_1_39]", R.mipmap.b39);
        EMOTION_B_MAP.put("[em_1_40]", R.mipmap.b40);
        EMOTION_B_MAP.put("[em_1_41]", R.mipmap.b41);
        EMOTION_B_MAP.put("[em_1_42]", R.mipmap.b42);
        EMOTION_B_MAP.put("[em_1_43]", R.mipmap.b43);
        EMOTION_B_MAP.put("[em_1_44]", R.mipmap.b44);
        EMOTION_B_MAP.put("[em_1_45]", R.mipmap.b45);
        EMOTION_B_MAP.put("[em_1_46]", R.mipmap.b46);
        EMOTION_B_MAP.put("[em_1_47]", R.mipmap.b47);
        EMOTION_B_MAP.put("[em_1_48]", R.mipmap.b48);
        EMOTION_B_MAP.put("[em_1_49]", R.mipmap.b49);


        EMOTION_C_MAP.put("[em_2_0]", R.mipmap.c00);
        EMOTION_C_MAP.put("[em_2_1]", R.mipmap.c01);
        EMOTION_C_MAP.put("[em_2_2]", R.mipmap.c02);
        EMOTION_C_MAP.put("[em_2_3]", R.mipmap.c03);
        EMOTION_C_MAP.put("[em_2_4]", R.mipmap.c04);
        EMOTION_C_MAP.put("[em_2_5]", R.mipmap.c05);
        EMOTION_C_MAP.put("[em_2_6]", R.mipmap.c06);
        EMOTION_C_MAP.put("[em_2_7]", R.mipmap.c07);
        EMOTION_C_MAP.put("[em_2_8]", R.mipmap.c08);
        EMOTION_C_MAP.put("[em_2_9]", R.mipmap.c09);
        EMOTION_C_MAP.put("[em_2_10]", R.mipmap.c10);
        EMOTION_C_MAP.put("[em_2_11]", R.mipmap.c11);
        EMOTION_C_MAP.put("[em_2_12]", R.mipmap.c12);
        EMOTION_C_MAP.put("[em_2_13]", R.mipmap.c13);
        EMOTION_C_MAP.put("[em_2_14]", R.mipmap.c14);
        EMOTION_C_MAP.put("[em_2_15]", R.mipmap.c15);
        EMOTION_C_MAP.put("[em_2_16]", R.mipmap.c16);
        EMOTION_C_MAP.put("[em_2_17]", R.mipmap.c17);
        EMOTION_C_MAP.put("[em_2_18]", R.mipmap.c18);
        EMOTION_C_MAP.put("[em_2_19]", R.mipmap.c19);
        EMOTION_C_MAP.put("[em_2_20]", R.mipmap.c20);
        EMOTION_C_MAP.put("[em_2_21]", R.mipmap.c21);
        EMOTION_C_MAP.put("[em_2_22]", R.mipmap.c22);
        EMOTION_C_MAP.put("[em_2_23]", R.mipmap.c23);
        EMOTION_C_MAP.put("[em_2_24]", R.mipmap.c24);
        EMOTION_C_MAP.put("[em_2_25]", R.mipmap.c25);
        EMOTION_C_MAP.put("[em_2_26]", R.mipmap.c26);
        EMOTION_C_MAP.put("[em_2_27]", R.mipmap.c27);
        EMOTION_C_MAP.put("[em_2_28]", R.mipmap.c28);
        EMOTION_C_MAP.put("[em_2_29]", R.mipmap.c29);
        EMOTION_C_MAP.put("[em_2_30]", R.mipmap.c30);
        EMOTION_C_MAP.put("[em_2_31]", R.mipmap.c31);
        EMOTION_C_MAP.put("[em_2_32]", R.mipmap.c32);
        EMOTION_C_MAP.put("[em_2_33]", R.mipmap.c33);
        EMOTION_C_MAP.put("[em_2_34]", R.mipmap.c34);
        EMOTION_C_MAP.put("[em_2_35]", R.mipmap.c35);
        EMOTION_C_MAP.put("[em_2_36]", R.mipmap.c36);
        EMOTION_C_MAP.put("[em_2_37]", R.mipmap.c37);
        EMOTION_C_MAP.put("[em_2_38]", R.mipmap.c38);
        EMOTION_C_MAP.put("[em_2_39]", R.mipmap.c39);
        EMOTION_C_MAP.put("[em_2_40]", R.mipmap.c40);
        EMOTION_C_MAP.put("[em_2_41]", R.mipmap.c41);
        EMOTION_C_MAP.put("[em_2_42]", R.mipmap.c42);
        EMOTION_C_MAP.put("[em_2_43]", R.mipmap.c43);
        EMOTION_C_MAP.put("[em_2_44]", R.mipmap.c44);
        EMOTION_C_MAP.put("[em_2_45]", R.mipmap.c45);
        EMOTION_C_MAP.put("[em_2_46]", R.mipmap.c46);
        EMOTION_C_MAP.put("[em_2_47]", R.mipmap.c47);
        EMOTION_C_MAP.put("[em_2_48]", R.mipmap.c48);
        EMOTION_C_MAP.put("[em_2_49]", R.mipmap.c49);
        EMOTION_C_MAP.put("[em_2_50]", R.mipmap.c50);
        EMOTION_C_MAP.put("[em_2_51]", R.mipmap.c51);
        EMOTION_C_MAP.put("[em_2_52]", R.mipmap.c52);
        EMOTION_C_MAP.put("[em_2_53]", R.mipmap.c53);

        EMOTION_D_MAP.put("[em_3_0]", R.mipmap.d00);
        EMOTION_D_MAP.put("[em_3_1]", R.mipmap.d01);
        EMOTION_D_MAP.put("[em_3_2]", R.mipmap.d02);
        EMOTION_D_MAP.put("[em_3_3]", R.mipmap.d03);
        EMOTION_D_MAP.put("[em_3_4]", R.mipmap.d04);
        EMOTION_D_MAP.put("[em_3_5]", R.mipmap.d05);
        EMOTION_D_MAP.put("[em_3_6]", R.mipmap.d06);
        EMOTION_D_MAP.put("[em_3_7]", R.mipmap.d07);
        EMOTION_D_MAP.put("[em_3_8]", R.mipmap.d08);
        EMOTION_D_MAP.put("[em_3_9]", R.mipmap.d09);
        EMOTION_D_MAP.put("[em_3_10]", R.mipmap.d10);
        EMOTION_D_MAP.put("[em_3_11]", R.mipmap.d11);
        EMOTION_D_MAP.put("[em_3_12]", R.mipmap.d12);
        EMOTION_D_MAP.put("[em_3_13]", R.mipmap.d13);
        EMOTION_D_MAP.put("[em_3_14]", R.mipmap.d14);
        EMOTION_D_MAP.put("[em_3_15]", R.mipmap.d15);
        EMOTION_D_MAP.put("[em_3_16]", R.mipmap.d16);
        EMOTION_D_MAP.put("[em_3_17]", R.mipmap.d17);
        EMOTION_D_MAP.put("[em_3_18]", R.mipmap.d18);
        EMOTION_D_MAP.put("[em_3_19]", R.mipmap.d19);
        EMOTION_D_MAP.put("[em_3_20]", R.mipmap.d20);
        EMOTION_D_MAP.put("[em_3_21]", R.mipmap.d21);
        EMOTION_D_MAP.put("[em_3_22]", R.mipmap.d22);
        EMOTION_D_MAP.put("[em_3_23]", R.mipmap.d23);
        EMOTION_D_MAP.put("[em_3_24]", R.mipmap.d24);
        EMOTION_D_MAP.put("[em_3_25]", R.mipmap.d25);
        EMOTION_D_MAP.put("[em_3_26]", R.mipmap.d26);
        EMOTION_D_MAP.put("[em_3_27]", R.mipmap.d27);
        EMOTION_D_MAP.put("[em_3_28]", R.mipmap.d28);
        EMOTION_D_MAP.put("[em_3_29]", R.mipmap.d29);
        EMOTION_D_MAP.put("[em_3_30]", R.mipmap.d30);
        EMOTION_D_MAP.put("[em_3_31]", R.mipmap.d31);
        EMOTION_D_MAP.put("[em_3_32]", R.mipmap.d32);
        EMOTION_D_MAP.put("[em_3_33]", R.mipmap.d33);
        EMOTION_D_MAP.put("[em_3_34]", R.mipmap.d34);
        EMOTION_D_MAP.put("[em_3_35]", R.mipmap.d35);
        EMOTION_D_MAP.put("[em_3_36]", R.mipmap.d36);
        EMOTION_D_MAP.put("[em_3_37]", R.mipmap.d37);
        EMOTION_D_MAP.put("[em_3_38]", R.mipmap.d38);
        EMOTION_D_MAP.put("[em_3_39]", R.mipmap.d39);
        EMOTION_D_MAP.put("[em_3_40]", R.mipmap.d40);
        EMOTION_D_MAP.put("[em_3_41]", R.mipmap.d41);
        EMOTION_D_MAP.put("[em_3_42]", R.mipmap.d42);
        EMOTION_D_MAP.put("[em_3_43]", R.mipmap.d43);
        EMOTION_D_MAP.put("[em_3_44]", R.mipmap.d44);


        EMOTION_F_MAP.put("[em_5_0]", R.mipmap.f00);
        EMOTION_F_MAP.put("[em_5_1]", R.mipmap.f01);
        EMOTION_F_MAP.put("[em_5_2]", R.mipmap.f02);
        EMOTION_F_MAP.put("[em_5_3]", R.mipmap.f03);
        EMOTION_F_MAP.put("[em_5_4]", R.mipmap.f04);
        EMOTION_F_MAP.put("[em_5_5]", R.mipmap.f05);
        EMOTION_F_MAP.put("[em_5_6]", R.mipmap.f06);
        EMOTION_F_MAP.put("[em_5_7]", R.mipmap.f07);
        EMOTION_F_MAP.put("[em_5_8]", R.mipmap.f08);
        EMOTION_F_MAP.put("[em_5_9]", R.mipmap.f09);
        EMOTION_F_MAP.put("[em_5_10]", R.mipmap.f10);
        EMOTION_F_MAP.put("[em_5_11]", R.mipmap.f11);
        EMOTION_F_MAP.put("[em_5_12]", R.mipmap.f12);
        EMOTION_F_MAP.put("[em_5_13]", R.mipmap.f13);
        EMOTION_F_MAP.put("[em_5_14]", R.mipmap.f14);
        EMOTION_F_MAP.put("[em_5_15]", R.mipmap.f15);


        EMOTION_G_MAP.put("[em_6_0]", R.mipmap.g00);
        EMOTION_G_MAP.put("[em_6_1]", R.mipmap.g01);
        EMOTION_G_MAP.put("[em_6_2]", R.mipmap.g02);
        EMOTION_G_MAP.put("[em_6_3]", R.mipmap.g03);
        EMOTION_G_MAP.put("[em_6_4]", R.mipmap.g04);
        EMOTION_G_MAP.put("[em_6_5]", R.mipmap.g05);
        EMOTION_G_MAP.put("[em_6_6]", R.mipmap.g06);
        EMOTION_G_MAP.put("[em_6_7]", R.mipmap.g07);
        EMOTION_G_MAP.put("[em_6_8]", R.mipmap.g08);
        EMOTION_G_MAP.put("[em_6_9]", R.mipmap.g09);
        EMOTION_G_MAP.put("[em_6_10]", R.mipmap.g10);
        EMOTION_G_MAP.put("[em_6_11]", R.mipmap.g11);
        EMOTION_G_MAP.put("[em_6_12]", R.mipmap.g12);
        EMOTION_G_MAP.put("[em_6_13]", R.mipmap.g13);
        EMOTION_G_MAP.put("[em_6_14]", R.mipmap.g14);
        EMOTION_G_MAP.put("[em_6_15]", R.mipmap.g15);
        EMOTION_G_MAP.put("[em_6_16]", R.mipmap.g16);
        EMOTION_G_MAP.put("[em_6_17]", R.mipmap.g17);
        EMOTION_G_MAP.put("[em_6_18]", R.mipmap.g18);
        EMOTION_G_MAP.put("[em_6_19]", R.mipmap.g19);
        EMOTION_G_MAP.put("[em_6_20]", R.mipmap.g20);
        EMOTION_G_MAP.put("[em_6_21]", R.mipmap.g21);
        EMOTION_G_MAP.put("[em_6_22]", R.mipmap.g22);
        EMOTION_G_MAP.put("[em_6_23]", R.mipmap.g23);
        EMOTION_G_MAP.put("[em_6_24]", R.mipmap.g24);
        EMOTION_G_MAP.put("[em_6_25]", R.mipmap.g25);
        EMOTION_G_MAP.put("[em_6_26]", R.mipmap.g26);
        EMOTION_G_MAP.put("[em_6_27]", R.mipmap.g27);
        EMOTION_G_MAP.put("[em_6_28]", R.mipmap.g28);
        EMOTION_G_MAP.put("[em_6_29]", R.mipmap.g29);
        EMOTION_G_MAP.put("[em_6_30]", R.mipmap.g30);

//        EMOTION_GG_MAP.put("[em_6_0]", R.mipmap.gg00);
//        EMOTION_GG_MAP.put("[em_6_1]", R.mipmap.gg01);
//        EMOTION_GG_MAP.put("[em_6_2]", R.mipmap.gg02);
//        EMOTION_GG_MAP.put("[em_6_3]", R.mipmap.gg03);
//        EMOTION_GG_MAP.put("[em_6_4]", R.mipmap.gg04);
//        EMOTION_GG_MAP.put("[em_6_5]", R.mipmap.gg05);
//        EMOTION_GG_MAP.put("[em_6_6]", R.mipmap.gg06);
//        EMOTION_GG_MAP.put("[em_6_7]", R.mipmap.gg07);
//        EMOTION_GG_MAP.put("[em_6_8]", R.mipmap.gg08);
//        EMOTION_GG_MAP.put("[em_6_9]", R.mipmap.gg09);
//        EMOTION_GG_MAP.put("[em_6_10]", R.mipmap.gg10);
//        EMOTION_GG_MAP.put("[em_6_11]", R.mipmap.gg11);
//        EMOTION_GG_MAP.put("[em_6_12]", R.mipmap.gg12);
//        EMOTION_GG_MAP.put("[em_6_13]", R.mipmap.gg13);
//        EMOTION_GG_MAP.put("[em_6_14]", R.mipmap.gg14);
//        EMOTION_GG_MAP.put("[em_6_15]", R.mipmap.gg15);
//        EMOTION_GG_MAP.put("[em_6_16]", R.mipmap.gg16);
//        EMOTION_GG_MAP.put("[em_6_17]", R.mipmap.gg17);
//        EMOTION_GG_MAP.put("[em_6_18]", R.mipmap.gg18);
//        EMOTION_GG_MAP.put("[em_6_19]", R.mipmap.gg19);
//        EMOTION_GG_MAP.put("[em_6_20]", R.mipmap.gg20);
//        EMOTION_GG_MAP.put("[em_6_21]", R.mipmap.gg21);
//        EMOTION_GG_MAP.put("[em_6_22]", R.mipmap.gg22);
//        EMOTION_GG_MAP.put("[em_6_23]", R.mipmap.gg23);
//        EMOTION_GG_MAP.put("[em_6_24]", R.mipmap.gg24);
//        EMOTION_GG_MAP.put("[em_6_25]", R.mipmap.gg25);
//        EMOTION_GG_MAP.put("[em_6_26]", R.mipmap.gg26);
//        EMOTION_GG_MAP.put("[em_6_27]", R.mipmap.gg27);
//        EMOTION_GG_MAP.put("[em_6_28]", R.mipmap.gg28);
//        EMOTION_GG_MAP.put("[em_6_29]", R.mipmap.gg29);
//        EMOTION_GG_MAP.put("[em_6_30]", R.mipmap.gg30);

       /* EMOTION_CLASSIC_MAP.put("[呵呵]", R.drawable.d_hehe);
        EMOTION_CLASSIC_MAP.put("[嘻嘻]", R.drawable.d_xixi);
        EMOTION_CLASSIC_MAP.put("[哈哈]", R.drawable.d_haha);
        EMOTION_CLASSIC_MAP.put("[爱你]", R.drawable.d_aini);
        EMOTION_CLASSIC_MAP.put("[挖鼻屎]", R.drawable.d_wabishi);
        EMOTION_CLASSIC_MAP.put("[吃惊]", R.drawable.d_chijing);
        EMOTION_CLASSIC_MAP.put("[晕]", R.drawable.d_yun);
        EMOTION_CLASSIC_MAP.put("[泪]", R.drawable.d_lei);
        EMOTION_CLASSIC_MAP.put("[馋嘴]", R.drawable.d_chanzui);
        EMOTION_CLASSIC_MAP.put("[抓狂]", R.drawable.d_zhuakuang);
        EMOTION_CLASSIC_MAP.put("[哼]", R.drawable.d_heng);
        EMOTION_CLASSIC_MAP.put("[可爱]", R.drawable.d_keai);
        EMOTION_CLASSIC_MAP.put("[怒]", R.drawable.d_nu);
        EMOTION_CLASSIC_MAP.put("[汗]", R.drawable.d_han);
        EMOTION_CLASSIC_MAP.put("[害羞]", R.drawable.d_haixiu);
        EMOTION_CLASSIC_MAP.put("[睡觉]", R.drawable.d_shuijiao);
        EMOTION_CLASSIC_MAP.put("[钱]", R.drawable.d_qian);
        EMOTION_CLASSIC_MAP.put("[偷笑]", R.drawable.d_touxiao);
        EMOTION_CLASSIC_MAP.put("[笑cry]", R.drawable.d_xiaoku);
        EMOTION_CLASSIC_MAP.put("[doge]", R.drawable.d_doge);
        EMOTION_CLASSIC_MAP.put("[喵喵]", R.drawable.d_miao);
        EMOTION_CLASSIC_MAP.put("[酷]", R.drawable.d_ku);
        EMOTION_CLASSIC_MAP.put("[衰]", R.drawable.d_shuai);
        EMOTION_CLASSIC_MAP.put("[闭嘴]", R.drawable.d_bizui);
        EMOTION_CLASSIC_MAP.put("[鄙视]", R.drawable.d_bishi);
        EMOTION_CLASSIC_MAP.put("[花心]", R.drawable.d_huaxin);
        EMOTION_CLASSIC_MAP.put("[鼓掌]", R.drawable.d_guzhang);
        EMOTION_CLASSIC_MAP.put("[悲伤]", R.drawable.d_beishang);
        EMOTION_CLASSIC_MAP.put("[思考]", R.drawable.d_sikao);
        EMOTION_CLASSIC_MAP.put("[生病]", R.drawable.d_shengbing);
        EMOTION_CLASSIC_MAP.put("[亲亲]", R.drawable.d_qinqin);
        EMOTION_CLASSIC_MAP.put("[怒骂]", R.drawable.d_numa);
        EMOTION_CLASSIC_MAP.put("[太开心]", R.drawable.d_taikaixin);
        EMOTION_CLASSIC_MAP.put("[懒得理你]", R.drawable.d_landelini);
        EMOTION_CLASSIC_MAP.put("[右哼哼]", R.drawable.d_youhengheng);
        EMOTION_CLASSIC_MAP.put("[左哼哼]", R.drawable.d_zuohengheng);
        EMOTION_CLASSIC_MAP.put("[嘘]", R.drawable.d_xu);
        EMOTION_CLASSIC_MAP.put("[委屈]", R.drawable.d_weiqu);
        EMOTION_CLASSIC_MAP.put("[吐]", R.drawable.d_tu);
        EMOTION_CLASSIC_MAP.put("[可怜]", R.drawable.d_kelian);
        EMOTION_CLASSIC_MAP.put("[打哈气]", R.drawable.d_dahaqi);
        EMOTION_CLASSIC_MAP.put("[挤眼]", R.drawable.d_jiyan);
        EMOTION_CLASSIC_MAP.put("[失望]", R.drawable.d_shiwang);
        EMOTION_CLASSIC_MAP.put("[顶]", R.drawable.d_ding);
        EMOTION_CLASSIC_MAP.put("[疑问]", R.drawable.d_yiwen);
        EMOTION_CLASSIC_MAP.put("[困]", R.drawable.d_kun);
        EMOTION_CLASSIC_MAP.put("[感冒]", R.drawable.d_ganmao);
        EMOTION_CLASSIC_MAP.put("[拜拜]", R.drawable.d_baibai);
        EMOTION_CLASSIC_MAP.put("[黑线]", R.drawable.d_heixian);
        EMOTION_CLASSIC_MAP.put("[阴险]", R.drawable.d_yinxian);
        EMOTION_CLASSIC_MAP.put("[打脸]", R.drawable.d_dalian);
        EMOTION_CLASSIC_MAP.put("[傻眼]", R.drawable.d_shayan);
        EMOTION_CLASSIC_MAP.put("[猪头]", R.drawable.d_zhutou);
        EMOTION_CLASSIC_MAP.put("[熊猫]", R.drawable.d_xiongmao);
        EMOTION_CLASSIC_MAP.put("[兔子]", R.drawable.d_tuzi);*/
    }

    private static void setEmptyMap(Map<String, Integer> map, int emotionType, int resId, int num) {
        for (int i = 0; i < num; i++) {
            map.put("[em_" + emotionType + "_" + i + "]", resId + i);
        }
    }

    /**
     * 根据名称获取当前表情图标R值
     *
     * @param EmotionType 表情类型标志符
     * @param imgName     名称
     * @return
     */
    public static int getImgByName(int EmotionType, String imgName) {
        Integer integer = null;
        switch (EmotionType) {
            case EMOTION_A_TYPE:
                integer = EMOTION_CLASSIC_MAP.get(imgName);
                break;
            case EMOTION_B_TYPE:
                integer = EMOTION_B_MAP.get(imgName);
                break;
            case EMOTION_BG_TYPE:
                integer = EMOTION_BG_MAP.get(imgName);
                break;
            case EMOTION_C_TYPE:
                integer = EMOTION_C_MAP.get(imgName);
                break;
            case EMOTION_CG_TYPE:
                integer = EMOTION_CG_MAP.get(imgName);
                break;
            case EMOTION_D_TYPE:
                integer = EMOTION_D_MAP.get(imgName);
                break;
            case EMOTION_DG_TYPE:
                integer = EMOTION_DG_MAP.get(imgName);
                break;
            case EMOTION_E_TYPE:
                integer = EMOTION_E_MAP.get(imgName);
                break;
            case EMOTION_EG_TYPE:
                integer = EMOTION_EG_MAP.get(imgName);
                break;
            case EMOTION_F_TYPE:
                integer = EMOTION_F_MAP.get(imgName);
                break;
            case EMOTION_G_TYPE:
                integer = EMOTION_G_MAP.get(imgName);
                break;
            case EMOTION_GG_TYPE:
                integer = EMOTION_GG_MAP.get(imgName);
                break;
            default:
                LogUtils.e("the emojiMap is null!!");
                break;
        }
        return integer == null ? -1 : integer;
    }

    /**
     * 根据类型获取表情数据
     *
     * @param EmotionType
     * @return
     */
    public static ArrayMap<String, Integer> getEmojiMap(int EmotionType) {
        ArrayMap<String, Integer> EmojiMap = null;
        switch (EmotionType) {
            case EMOTION_A_TYPE:
                EmojiMap = EMOTION_CLASSIC_MAP;
                break;
            case EMOTION_B_TYPE:
                EmojiMap = EMOTION_B_MAP;
                break;
            case EMOTION_C_TYPE:
                EmojiMap = EMOTION_C_MAP;
                break;
            case EMOTION_D_TYPE:
                EmojiMap = EMOTION_D_MAP;
                break;
            case EMOTION_E_TYPE:
                EmojiMap = EMOTION_E_MAP;
                break;
            case EMOTION_F_TYPE:
                EmojiMap = EMOTION_F_MAP;
                break;
            case EMOTION_G_TYPE:
                EmojiMap = EMOTION_G_MAP;
                break;
            default:
                // EmojiMap = EMOTION_B_MAP;
                EmojiMap = EMPTY_MAP;
                break;
        }
        return EmojiMap;
    }
}
