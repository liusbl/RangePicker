package utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import telesoftas.rangebartest.R;

public class CustomSettingsView extends ConstraintLayout {
    @BindView(R.id.settingsLabelTextView) TextView settingsLabelTextView;
    @BindView(R.id.settingsValueEditText) EditText settingsValueEditText;
    @BindView(R.id.settingsValueSpinner) Spinner settingsValueSpinner;
    @BindView(R.id.settingsMeasurementUnit) TextView settingsMeasurementUnit;

    public CustomSettingsView(Context context) {
        super(context);
    }

    public CustomSettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomSettingsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void init(AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.settings_item, this, true);
        ButterKnife.bind(view, this);
        setFields(attributeSet);
    }

    private void setFields(AttributeSet attributeSet) {
        TypedArray attributes = getContext().obtainStyledAttributes(attributeSet,
                R.styleable.CustomSettingsView);
        setLabel(attributes.getString(R.styleable.CustomSettingsView_settings_label));
        setMeasurementUnit(attributes.getString(
                R.styleable.CustomSettingsView_settings_measurement_unit));
        setValue(attributes.getString(R.styleable.CustomSettingsView_settings_value));
        setMode(attributes.getInt(R.styleable.CustomSettingsView_settings_mode, 0));
        attributes.recycle();
    }

    public void setLabel(String text) {
        settingsLabelTextView.setText(text);
    }

    public void setValue(String value) {
        settingsValueEditText.setText(value);
    }

    public void setMode(int mode) {
//        if (mode == 0) {
//            settingsValueEditText.setVisibility(VISIBLE);
//            settingsValueSpinner.setVisibility(GONE);
//        } else {
//            settingsValueEditText.setVisibility(GONE);
//            settingsValueSpinner.setVisibility(VISIBLE);
//        }
    }

    public void getValue() {
        settingsValueEditText.getText();
    }

    public void setMeasurementUnit(String text) {
        settingsMeasurementUnit.setText(text);
    }
}