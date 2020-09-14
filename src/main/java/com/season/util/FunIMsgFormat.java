package com.season.util;
public class FunIMsgFormat extends AbstractGaeaLogMsgFormat {

    private static final String FUNI_HEAD = "FUNI";

    public static final String HEAD = FUNI_HEAD + INFO_DIV;

    public static enum MsgStyle {

     
        DEFAULT_LOG(FUNI_HEAD + INFO_DIV + "DEFAULT_LOG"
                + INFO_DIV + LOG_ARGUMENT),

    
        ERROR_REPORT(FUNI_HEAD + INFO_DIV + "ERROR_REPORT"
                + INFO_DIV + LOG_ARGUMENT
                + INFO_DIV + LOG_ARGUMENT),

   
        STATUS_OF_ERRORS_FROM_START(FUNI_HEAD + INFO_DIV
                + "STATUS_OF_ERRORS_FROM_START" + INFO_DIV
                + LOG_ARGUMENT + INFO_DIV
                + LOG_ARGUMENT),

   
        STATUS_OF_LAST_ERRORS(FUNI_HEAD + INFO_DIV
                + "STATUS_OF_LAST_ERRORS" + INFO_DIV
                + LOG_ARGUMENT + INFO_DIV
                + LOG_ARGUMENT);

        private final String format;

        private MsgStyle() {
            this.format = FUNI_HEAD + INFO_DIV
                    + LOG_ARGUMENT;
        }

        private MsgStyle(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

        public String getFormat(String selfFormat) {
            return FUNI_HEAD + INFO_DIV + "DEFAULT_LOG"
                    + INFO_DIV + selfFormat;
        }
    }
}
