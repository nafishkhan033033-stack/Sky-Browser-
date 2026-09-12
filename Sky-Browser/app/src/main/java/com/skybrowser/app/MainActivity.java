package com.skybrowser.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private WebView webView;
    private EditText addressBar;

    private final ArrayList<String> history = new ArrayList<>();
    private final ArrayList<String> bookmarks = new ArrayList<>();

    private static final String HOME =
            "file:///android_asset/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createBrowserUI();

        webView.loadUrl(HOME);
    }

    private void createBrowserUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        // Top title
        TextView title = new TextView(this);
        title.setText("☁ SkyBrowser");
        title.setTextSize(20);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setPadding(20, 0, 10, 0);
        title.setBackgroundColor(Color.rgb(25, 118, 210));

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55
                )
        );

        // Address bar row
        LinearLayout addressRow = new LinearLayout(this);
        addressRow.setOrientation(LinearLayout.HORIZONTAL);
        addressRow.setPadding(8, 8, 8, 8);
        addressRow.setBackgroundColor(Color.rgb(245, 245, 245));

        addressBar = new EditText(this);
        addressBar.setSingleLine(true);
        addressBar.setHint("Search or enter website");
        addressBar.setTextSize(15);
        addressBar.setPadding(18, 0, 18, 0);

        addressRow.addView(
                addressBar,
                new LinearLayout.LayoutParams(
                        0,
                        50,
                        1
                )
        );

        Button goButton = new Button(this);
        goButton.setText("GO");
        goButton.setTextSize(12);

        addressRow.addView(
                goButton,
                new LinearLayout.LayoutParams(
                        65,
                        50
                )
        );

        root.addView(addressRow);

        // Navigation buttons
        LinearLayout controls = new LinearLayout(this);
        controls.setOrientation(LinearLayout.HORIZONTAL);
        controls.setGravity(Gravity.CENTER);

        Button backButton = makeButton("‹");
        Button forwardButton = makeButton("›");
        Button reloadButton = makeButton("↻");
        Button homeButton = makeButton("⌂");
        Button bookmarkButton = makeButton("★");
        Button historyButton = makeButton("History");

        controls.addView(backButton);
        controls.addView(forwardButton);
        controls.addView(reloadButton);
        controls.addView(homeButton);
        controls.addView(bookmarkButton);
        controls.addView(historyButton);

        root.addView(
                controls,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        55
                )
        );

        // WebView
        webView = new WebView(this);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request) {

                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(
                    WebView view,
                    String url) {

                addressBar.setText(url);

                if (!url.equals(HOME)) {
                    history.add(url);
                }
            }
        });

        root.addView(
                webView,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(root);

        // GO
        goButton.setOnClickListener(v -> openAddress());

        // Keyboard search
        addressBar.setOnEditorActionListener(
                (v, actionId, event) -> {

                    if (actionId == EditorInfo.IME_ACTION_GO ||
                            actionId == EditorInfo.IME_ACTION_SEARCH ||
                            (event != null &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                        openAddress();
                        return true;
                    }

                    return false;
                }
        );

        // Back
        backButton.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        });

        // Forward
        forwardButton.setOnClickListener(v -> {
            if (webView.canGoForward()) {
                webView.goForward();
            }
        });

        // Reload
        reloadButton.setOnClickListener(v -> webView.reload());

        // Home
        homeButton.setOnClickListener(v -> {
            addressBar.setText("");
            webView.loadUrl(HOME);
        });

        // Bookmark
        bookmarkButton.setOnClickListener(v -> addBookmark());

        // History
        historyButton.setOnClickListener(v -> showHistory());
    }

    private Button makeButton(String text) {

        Button button = new Button(this);
        button.setText(text);
        button.setTextSize(12);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        50,
                        1
                );

        button.setLayoutParams(params);

        return button;
    }

    private void openAddress() {

        String input = addressBar.getText()
                .toString()
                .trim();

        if (input.isEmpty()) {
            return;
        }

        String url;

        if (input.startsWith("http://") ||
                input.startsWith("https://")) {

            url = input;

        } else if (input.contains(".") &&
                !input.contains(" ")) {

            url = "https://" + input;

        } else {

            try {

                String query = URLEncoder.encode(
                        input,
                        "UTF-8"
                );

                url = "https://www.google.com/search?q="
                        + query;

            } catch (Exception e) {

                url = "https://www.google.com";
            }
        }

        webView.loadUrl(url);
    }

    private void addBookmark() {

        String url = webView.getUrl();

        if (url == null || url.equals(HOME)) {

            Toast.makeText(
                    this,
                    "Open a website first",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (!bookmarks.contains(url)) {

            bookmarks.add(url);

            Toast.makeText(
                    this,
                    "Bookmark saved",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Already bookmarked",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void showHistory() {

        if (history.isEmpty()) {

            Toast.makeText(
                    this,
                    "No history yet",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        StringBuilder text = new StringBuilder();

        for (int i = history.size() - 1; i >= 0; i--) {

            text.append(history.get(i))
                    .append("\n\n");
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("SkyBrowser History")
                .setMessage(text.toString())
                .setPositiveButton("Close", null)
                .show();
    }

    @Override
    public void onBackPressed() {

        if (webView != null &&
                webView.canGoBack()) {

            webView.goBack();

        } else {

            super.onBackPressed();
        }
    }
}
