package in.technozion.technozion;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.apache.http.util.EncodingUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class WebViewActivityFragment extends Fragment {

    private String string;
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
        string = getActivity().getIntent().getStringExtra("data");
        WebView webView= (WebView) getActivity().findViewById(R.id.webViewPayment);
        //byte[] post = EncodingUtils.getBytes("txnid=05ad29634b89d840eb32&amount=6702&salt=vlqHgOcF&productinfo=Technozion Registration&email=rashood.khan@gmail.com&phone=9160929965&firstname=Rashid&surl=http://localhost/tz-registration-master/transactions/success_mobile&curl=http://localhost/tz-registration-master/transactions/cancel_mobile&furl=http://localhost/tz-registration-master/transactions/failure_mobile&tourl=http://localhost/tz-registration-master/transactions/timeout_mobile&drop_category=EMI&COD&key=I2XJqe&PAYU_BASE_URL=https://secure.payu.in&action=https://secure.payu.in/_payment&hash=46f771469dabf753330cf89b8951450475d418057b0d634008a0db54c62904c7623e0954630da8c7b9d16cb9e05e470b647b31adf452dac16934d826c0246e0b","BASE64");
        //byte[] post = EncodingUtils.getBytes("hi=hello", "BASE64");

        try {
            JSONObject jsonObject= new JSONObject(string);
            String txnid = jsonObject.getString("txnid");
            String amount = String.valueOf(jsonObject.getInt("amount"));
            String salt = jsonObject.getString("salt");
            String productinfo = jsonObject.getString("productinfo");
            String email = jsonObject.getString("email");
            String phone = jsonObject.getString("phone");
            String firstname = jsonObject.getString("firstname");
            String surl = jsonObject.getString("surl");
            String curl = jsonObject.getString("curl");
            String furl = jsonObject.getString("furl");
            String tourl = jsonObject.getString("tourl");
            String drop_category = jsonObject.getString("drop_category");
            String key = jsonObject.getString("key");
            String PAYU_BASE_URL = jsonObject.getString("PAYU_BASE_URL");
            String action = jsonObject.getString("action");
            String hash = jsonObject.getString("hash");
            surl = surl.replace("\\","");
            curl = curl.replace("\\","");
            furl = furl.replace("\\","");
            tourl = tourl.replace("\\","");
            PAYU_BASE_URL = PAYU_BASE_URL.replace("\\","");
            action = action.replace("\\","");
            byte[] post = EncodingUtils.getBytes("key="+key+"&hash="+hash+"&txnid="+txnid+"&amount="+amount+
                    "&productinfo="+productinfo+"&firstname="+firstname+"&email="+email+"&phone="+phone+"&surl="+surl+
                    "&curl="+curl+"&furl="+furl+"&tourl="+tourl+ "&drop_category="+drop_category,"BASE64");
            webView.postUrl(action, post);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
