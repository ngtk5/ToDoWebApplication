package com.ngtk5.todoapp.annotations;

import java.time.LocalDate;
import java.util.Objects;

import com.ngtk5.todoapp.forms.NewTaskForm;
import com.ngtk5.todoapp.forms.TaskForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateCheckImpl implements ConstraintValidator<DateCheck, Object> {

    private String message; // エラーメッセージ

    @Override
    public void initialize(DateCheck constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (Objects.isNull(obj)) {
            return true;
        }
        if (obj instanceof NewTaskForm) {
            // NewTaskForm型の場合
            NewTaskForm form = (NewTaskForm) obj;
            return errorCheck(form.getDeadline(), LocalDate.now(), context);
        } else if (obj instanceof TaskForm) {
            // TaskForm型の場合
            TaskForm form = (TaskForm) obj;
            if (form.isCompletedFlg()) {
                return true;    // trueならこれ以上の処理をしない
            }
            return errorCheck(form.getDeadline(), LocalDate.now(), context);
        } else {
            // 想定外のデータ型なら例外を発生
            throw new IllegalArgumentException("このデータ型はサポートされていません");
        }
    }

    private boolean errorCheck(LocalDate deadline, LocalDate currentDate, ConstraintValidatorContext context) {
        // null or 空文字なら何もしない
        if (Objects.isNull(deadline) || deadline.toString().isEmpty()) {
            return true;
        }
        // 今日以降の期限が設定されているならこれ以上の処理をしない
        if (deadline.isAfter(currentDate) || deadline.isEqual(currentDate)) {
            return true;
        }
        // デフォルトの制約違反情報をクリア
        context.disableDefaultConstraintViolation();
        // エラーメッセージをdeadlineにセット
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode("deadline")
            .addConstraintViolation();
        return false;
    }
}
