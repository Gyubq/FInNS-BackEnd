package com.finns.record;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DummyDataGenerator {

    public static void main(String[] args) {
        try (FileWriter file = new FileWriter("dummy_record_feed_data.json")) {
            file.write("[\n");
            Random random = new Random();
            String[] categories = {"식비", "쇼핑", "미용", "의료", "통신", "교통" ,"문화 · 여행","교육","술 · 유흥","기타"};
            String[] places = {"스타벅스", "CGV", "이디야 커피", "다이소", "홈플러스", "마트"};

            for (int i = 1; i <= 1000; i++) {
                String jsonRecord = String.format(
                        "{\"record_no\": %d, \"user_no\": %d, \"card_no\": %d, \"public_status\": %b, " +
                                "\"category\": \"%s\", \"memo\": \"메모 %d\", \"amount\": %d, \"place\": \"%s\", " +
                                "\"transaction_date\": \"%s\", \"great_count\": %d, \"stupid_count\": %d, \"renew_status\": %b}",
                        i, random.nextInt(10) + 1, random.nextInt(5) + 1, random.nextBoolean(),
                        categories[random.nextInt(categories.length)], i, random.nextInt(49000) + 1000,
                        places[random.nextInt(places.length)], "2023-10-" + (random.nextInt(30) + 1),
                        random.nextInt(100), random.nextInt(100), random.nextBoolean());

                file.write(jsonRecord + (i < 1000 ? "," : "") + "\n");
            }

            file.write("]");
            System.out.println("더미 데이터가 생성되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
