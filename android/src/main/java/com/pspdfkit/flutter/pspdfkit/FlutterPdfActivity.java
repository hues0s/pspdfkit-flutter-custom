package com.pspdfkit.flutter.pspdfkit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pspdfkit.annotations.measurements.FloatPrecision;
import com.pspdfkit.annotations.measurements.Scale;
import com.pspdfkit.document.PdfDocument;
import com.pspdfkit.ui.PdfActivity;

import java.util.concurrent.atomic.AtomicReference;

import io.flutter.plugin.common.MethodChannel.Result;

/**
 * For communication with the PSPDFKit plugin, we keep a static reference to the current
 * activity.
 */
public class FlutterPdfActivity extends PdfActivity {

    @Nullable private static FlutterPdfActivity currentActivity;
    @NonNull private static final AtomicReference<Result> loadedDocumentResult = new AtomicReference<>();

    @Nullable private static Scale scale;
    @Nullable private static FloatPrecision floatPrecision;

    public static void setLoadedDocumentResult(Result result) {
        loadedDocumentResult.set(result);
    }

    public static void setMeasurementScale(@Nullable  final Scale scale) {
        FlutterPdfActivity.scale = scale;
    }

    public static void setFloatPrecision(@Nullable final FloatPrecision floatPrecision) {
        FlutterPdfActivity.floatPrecision = floatPrecision;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        bindActivity();
    }

    @Override
    protected void onPause() {
        // Notify the Flutter PSPDFKit plugin that the activity is going to enter the onPause state.
        EventDispatcher.getInstance().notifyActivityOnPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseActivity();
    }

    @Override
    public void onDocumentLoaded(@NonNull PdfDocument pdfDocument) {
        super.onDocumentLoaded(pdfDocument);
        Result result = loadedDocumentResult.getAndSet(null);
        if (result != null) {
            result.success(true);
        }

        if (scale != null) {
            pdfDocument.setMeasurementScale(scale);
        }

        if (floatPrecision != null) {
            pdfDocument.setMeasurementPrecision(floatPrecision);
        }
    }

    @Override
    public void onDocumentLoadFailed(@NonNull Throwable throwable) {
        super.onDocumentLoadFailed(throwable);
        Result result = loadedDocumentResult.getAndSet(null);
        if (result != null) {
            result.success(false);
        }
    }

    private void bindActivity() {
        currentActivity = this;
    }

    private void releaseActivity() {
        Result result = loadedDocumentResult.getAndSet(null);
        if (result != null) {
            result.success(false);
        }
        currentActivity = null;
    }

    @Nullable
    public static FlutterPdfActivity getCurrentActivity() {
        return currentActivity;
    }
}
