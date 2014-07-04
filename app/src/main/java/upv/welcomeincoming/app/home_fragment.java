package upv.welcomeincoming.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.List;

import util.RSS.FeedParser;
import util.RSS.FeedParserFactory;
import util.RSS.ParserType;
import util.RSS.RssListAdapter;
import util.RSS.Message;

public class home_fragment extends ListFragment {
    private Activity local;
    RssListAdapter adapter;
    private List<Message> messages;
        @Override
        public View onCreateView( LayoutInflater inflater, ViewGroup  container, Bundle savedInstanceState) {
            View view =  inflater.inflate(R.layout.fragment_home, container,false);
            new RetrieveFeedTask().execute();

            return view;
        }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent viewMessage = new Intent(Intent.ACTION_VIEW,
                Uri.parse(messages.get(position).getLink().toExternalForm()));
        this.startActivity(viewMessage);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private Exception exception;

        @Override
        protected void onPostExecute(Void v) {
            setListAdapter(new RssListAdapter(getActivity(), 0, messages));
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {

                Log.e("RSS", "Llego al background");
                FeedParser parser = FeedParserFactory.getParser(ParserType.ANDROID_SAX);
                messages = parser.parse();
                Log.e("mensajes", messages.get(1).getTitle());
                Log.e("mensajes", messages.get(2).getTitle());
                Log.e("mensajes", messages.get(3).getTitle());
                Log.e("mensajes", messages.get(4).getTitle());
            } catch (Exception e) {
                Log.e("RSS ERROR", e.getMessage());
            }
            return null;
        }
    }
}
