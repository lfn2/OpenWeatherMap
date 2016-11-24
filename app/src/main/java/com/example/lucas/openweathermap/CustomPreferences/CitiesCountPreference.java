package com.example.lucas.openweathermap.CustomPreferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import com.example.lucas.openweathermap.R;

/**
 * Created by Lucas on 24/11/2016.
 */

public class CitiesCountPreference extends DialogPreference {

    private final int DEFAULT_VALUE = 15;
    private final int MIN_VALUE = 1;
    private final int MAX_VALUE = 30;

    private int value;
    private NumberPicker picker = null;

    public CitiesCountPreference(Context context) {
        this(context, null);
    }

    public CitiesCountPreference(Context context, AttributeSet attributes) {
        this(context, attributes, android.R.attr.dialogPreferenceStyle);
    }

    public CitiesCountPreference(Context context, AttributeSet attributes, int defStyle) {
        super(context, attributes, defStyle);

        setPositiveButtonText(R.string.string_ok);
        setNegativeButtonText(R.string.string_cancel);
        this.value = DEFAULT_VALUE;
    }

    @Override
    protected View onCreateDialogView() {
        this.picker = new NumberPicker(getContext());
        this.picker.setMinValue(MIN_VALUE);
        this.picker.setMaxValue(MAX_VALUE);
        return this.picker;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult) {
            this.value = picker.getValue();

            setSummary(getSummary());
            if(callChangeListener(this.value)) {
                persistInt(this.value);

                notifyChanged();
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        if (restoreValue) {
            if (defaultValue == null)
                this.value = getPersistedInt(DEFAULT_VALUE);
            else
                this.value = Integer.parseInt(getPersistedString((String) defaultValue));
        }
        else {
            if (defaultValue == null)
                this.value = DEFAULT_VALUE;
            else
                this.value = Integer.parseInt((String) defaultValue);
        }

        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        return this.value + "";
    }

}
