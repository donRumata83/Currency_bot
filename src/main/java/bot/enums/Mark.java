package bot.enums;

public enum Mark {
    USD("\uD83D\uDCB5"), EUR("\uD83D\uDCB6"), RUB("");

    private String mark;

    Mark(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return this.mark;
    }
}
