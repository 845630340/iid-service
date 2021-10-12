package com.inspur.cloud.common.exception;

import com.inspur.cloud.common.exception.model.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author mysterious guest
 **/
public class AssertUtils extends Assert {
    private AssertUtils() {
    }


    /**
     * when expression is not true,throw e
     */
    public static void isTrue(boolean expression, String code) {
        if (!expression) {
            throw new ServiceException(code);
        }
    }

    public static void isTrue(boolean expression, String code, HttpStatus httpStatus) {
        if (!expression) {
            throw new ServiceException(httpStatus, code);
        }
    }

    public static void isTrue(boolean expression, String code, String message, HttpStatus httpStatus) {
        if (!expression) {
            throw new ServiceException(httpStatus, code, message);
        }
    }


    public static void isFalse(boolean expression, String code, HttpStatus httpStatus) {
        if (expression) {
            throw new ServiceException(httpStatus, code);
        }
    }

    public static void isFalse(boolean expression, String code, String message, HttpStatus httpStatus) {
        if (expression) {
            throw new ServiceException(httpStatus, code, message);
        }
    }

    /**
     * when object is not null,throw e
     */
    public static void isNull(@Nullable Object object,@NonNull String code) {
        if (object != null) {
            throw new ServiceException(code);
        }
    }

    public static void isNull(@Nullable Object object, String code, HttpStatus httpStatus) {
        if (object != null) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when object is null,throw e
     */
    public static void notNull(@Nullable Object object, String code) {
        if (object == null) {
            throw new ServiceException(code);
        }
    }

    public static void notNull(@Nullable Object object, String code, HttpStatus httpStatus) {
        if (object == null) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when text is null or text is "" ,throw e
     */
    public static void hasLength(@Nullable String text,@NonNull String code) {
        if (!StringUtils.hasLength(text)) {
            throw new ServiceException(code);
        }
    }

    public static void hasLength(@Nullable String text, String code, HttpStatus httpStatus) {
        if (!StringUtils.hasLength(text)) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when text is null or text is "" or text is all whitespace ,throw e
     */
    public static void hasText(@Nullable String text, @NonNull String code) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(code);
        }
    }

    public static void hasText(@Nullable String text, String code, HttpStatus httpStatus) {
        if (!StringUtils.hasText(text)) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when textToSearch contains substring ,throw e
     */
    public static void doesNotContain(@Nullable String textToSearch, @Nullable String substring, @NonNull String code) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(code);
        }
    }

    public static void doesNotContain(@Nullable String textToSearch, String substring, String code, HttpStatus httpStatus) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when array is null or empty ,throw e
     */
    public static void notEmpty(@Nullable Object[] array,@NonNull String code) {
        if (ObjectUtils.isEmpty(array)) {
            throw new ServiceException(code);
        }
    }

    public static void notEmpty(@Nullable Object[] array, String code, HttpStatus httpStatus) {
        if (ObjectUtils.isEmpty(array)) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when array has null element ,throw e
     */
    public static void noNullElements(@Nullable Object[] array,@NonNull String code) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(code);
                }
            }
        }
    }

    public static void noNullElements(@Nullable Object[] array, String code, HttpStatus httpStatus) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new ServiceException(httpStatus, code);
                }
            }
        }
    }

    /**
     * when collection is null or empty ,throw e
     */
    public static void notEmpty(@Nullable Collection<?> collection,@NonNull String code) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(code);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String code, HttpStatus httpStatus) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(httpStatus, code);
        }
    }

    /**
     * when map is null or empty ,throw e
     */
    public static void notEmpty(@Nullable Map<?, ?> map,@NonNull String code) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(code);
        }
    }

    public static void notEmpty(@Nullable Map<?, ?> map, String code, HttpStatus httpStatus) {
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(httpStatus, code);
        }
    }

}
