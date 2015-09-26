package in.technozion.technozion;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.apache.http.util.EncodingUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class WebViewActivityFragment extends Fragment {

    public WebViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WebView webView= (WebView) getActivity().findViewById(R.id.webViewPayment);

//        webView.loadUrl("http://google.com");
        byte[] post = EncodingUtils.getBytes("txnid=05ad29634b89d840eb32&amount=6702&salt=vlqHgOcF&productinfo=Technozion Registration&email=rashood.khan@gmail.com&phone=9160929965&firstname=Rashid&surl=http://localhost/tz-registration-master/transactions/success_mobile&curl=http://localhost/tz-registration-master/transactions/cancel_mobile&furl=http://localhost/tz-registration-master/transactions/failure_mobile&tourl=http://localhost/tz-registration-master/transactions/timeout_mobile&drop_category=EMI&COD&key=I2XJqe&PAYU_BASE_URL=https://secure.payu.in&action=https://secure.payu.in/_payment&hash=46f771469dabf753330cf89b8951450475d418057b0d634008a0db54c62904c7623e0954630da8c7b9d16cb9e05e470b647b31adf452dac16934d826c0246e0b","BASE64");
        //byte[] post = EncodingUtils.getBytes("hi=hello", "BASE64");
        webView.postUrl("https://secure.payu.in/_payment", post);

    }
}
