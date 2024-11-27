package store.model;

import camp.nextstep.edu.missionutils.DateTimes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PromotionPolicy {
    private int promotionBuyCount;
    private int promotionGetCount;

    private String startDate;
    private String endDate;

    public PromotionPolicy(String productPromotion) {
        promotionBuyCount = 0;
        promotionGetCount = 0;
        startDate = "9999-12-31";
        endDate = "9999-12-31";
        initPromotionInfo(productPromotion);
    }

    private void initPromotionInfo(String productPromotion) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/promotions.md"));
            String readPromotion;

            while ((readPromotion = reader.readLine()) != null) {
                String[] promotionInfo = readPromotion.split(",");
                String promotionName = promotionInfo[0];
                if (promotionName.equals(productPromotion)) {
                    promotionBuyCount = Integer.parseInt(promotionInfo[1]);
                    promotionGetCount = Integer.parseInt(promotionInfo[2]);
                    startDate = promotionInfo[3];
                    endDate = promotionInfo[4];
                    reader.close();
                    return;
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public boolean isValidPeriod() {
        LocalDateTime targetTime = DateTimes.now();

        String[] startInfo = startDate.split("-");
        String[] endInfo = endDate.split("-");

        LocalDate targetDate = LocalDate.of(targetTime.getYear(), targetTime.getMonth(), targetTime.getDayOfMonth());
        LocalDate startDate = LocalDate.of(Integer.parseInt(startInfo[0]), Integer.parseInt(startInfo[1]), Integer.parseInt(startInfo[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endInfo[0]), Integer.parseInt(endInfo[1]), Integer.parseInt(endInfo[2]));
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    public int getPromotionCount() {
        return promotionBuyCount + promotionGetCount;
    }

    public int getGiftCount(int quantity) {
        return quantity / getPromotionCount();
    }

    public int getPromotionBuyCount() {
        return promotionBuyCount;
    }

    public int getPromotionGetCount() {
        return promotionGetCount;
    }
}
