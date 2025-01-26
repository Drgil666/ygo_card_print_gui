package entity;

import lombok.Data;

/**
 * @author DrGilbert
 * @date 2025/01/16 20:05
 */
@Data
public class Card {
    private Integer code;
    private String message;
    private Integer total;
    private Long timestamp;
    private CardItem data;
    @Data
    public class CardItem {
        private String id;
        private String name;
        private String desc;
        private Integer atk;
        private Integer def;
        private Integer type;
        private Integer race;
        private Integer level;
        private Integer attribute;
    }
}
