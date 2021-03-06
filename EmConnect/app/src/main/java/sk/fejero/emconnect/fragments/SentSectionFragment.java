package sk.fejero.emconnect.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sk.fejero.emconnect.R;
import sk.fejero.emconnect.management.ContainerManagement;
import sk.fejero.emconnect.management.DataLoader;

import sk.fejero.emconnect.messages.Message;

/**
 * Created by fejero on 23.10.2014.
 */
public class SentSectionFragment extends Fragment {

    private View linkView;
    private View messageView;
    private View contentView;

    private int defaultTextColor;
    private TextView currentLinkView;
    private int currentLinkColor = Color.RED;
    private DataLoader loader;
    private ContainerManagement cm;
    private boolean messageViewEnabled;
    private LayoutInflater inflater;
    private ViewGroup container;
    private ViewPager viewPager;


    public SentSectionFragment() {

    }

    public void loadLoader(DataLoader loader, ContainerManagement cm, ViewPager viewPager){
        this.loader = loader;
        this.cm = cm;
        this.viewPager = viewPager;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;
        this.container = container;
        messageViewEnabled=false;

        View rootView = inflater.inflate(R.layout.fragment_sent, container, false);

        LinearLayout contentParentLayout = (LinearLayout)rootView.findViewById(R.id.sent_list);
        LinearLayout leftBarParentLayout = (LinearLayout)rootView.findViewById(R.id.sent_link_list_layout);

        initSideBar(inflater,leftBarParentLayout,contentParentLayout);
        loader.loadSent(cm);
        loader.loadConcepts(cm);
        initSentContent(inflater, contentParentLayout,leftBarParentLayout);

        return rootView;
    }

    private void clearFragmentContent(LinearLayout leftBarParentLayout, final LinearLayout contentParentLayout){
        leftBarParentLayout.removeAllViews();
        contentParentLayout.removeAllViews();
        //initMessageViewLayout(leftBarParentLayout,contentParentLayout);
    }

    private void initialView(LinearLayout leftBarParentLayout, LinearLayout contentParentLayout){
        messageViewEnabled=false;
        leftBarParentLayout.removeAllViews();
        contentParentLayout.removeAllViews();

        initSideBar(inflater, leftBarParentLayout, contentParentLayout);
        loader.loadInbox(cm);
        loader.loadSpam(cm);
        loader.loadTrash(cm);
        initSentContent(inflater, contentParentLayout, leftBarParentLayout);
    }

    private void initSideBar(final LayoutInflater inflater, final LinearLayout leftBarParentLayout, final LinearLayout contentParentLayout){
        linkView = inflater.inflate(R.layout.left_bar_link_layout, leftBarParentLayout, false);
        LinearLayout linkLayout = (LinearLayout)linkView.findViewById(R.id.link_content_layout);
        final TextView sentLinkTextView = (TextView)linkView.findViewById(R.id.link_content);
        sentLinkTextView.setText("Sent");

        final int currentTextColor = sentLinkTextView.getCurrentTextColor();
        currentLinkView = sentLinkTextView;

        defaultTextColor = currentTextColor;
        currentLinkView = sentLinkTextView;
        sentLinkTextView.setTextColor(currentLinkColor);


        sentLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLinkView.setTextColor(defaultTextColor);
                sentLinkTextView.setTextColor(currentLinkColor);
                currentLinkView = sentLinkTextView;
                contentParentLayout.removeViewAt(0);
                initSentContent(inflater,contentParentLayout,leftBarParentLayout);
            }
        });
        leftBarParentLayout.addView(linkLayout);

        linkView = inflater.inflate(R.layout.left_bar_link_layout, leftBarParentLayout, false);
        linkLayout = (LinearLayout)linkView.findViewById(R.id.link_content_layout);
        final TextView conceptsLinkTextView = (TextView)linkView.findViewById(R.id.link_content);
        conceptsLinkTextView.setText("Concepts");
        conceptsLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLinkView.setTextColor(defaultTextColor);
                conceptsLinkTextView.setTextColor(currentLinkColor);
                currentLinkView = conceptsLinkTextView;
                contentParentLayout.removeViewAt(0);
                initConceptsContent(inflater,contentParentLayout,leftBarParentLayout);
            }
        });
        leftBarParentLayout.addView(linkLayout);
    }

    private void initSentContent(LayoutInflater inflater,final LinearLayout contentParentLayout,final LinearLayout leftBarParentLayout){
        contentView = inflater.inflate(R.layout.sent_list_layout, contentParentLayout, false);

        LinearLayout contentScrollView = (LinearLayout)contentView.findViewById(R.id.sentScrollLayout);
        LinearLayout contentScrollLayout = (LinearLayout)contentView.findViewById(R.id.sent_list_layout);
        contentParentLayout.addView(contentScrollView);



        for (Message m : cm.getSentMessageList()){
            final Message actualM = m;
            messageView = inflater.inflate(R.layout.single_inbox_layout, contentScrollLayout, false);
            LinearLayout textViewLayout = (LinearLayout)messageView.findViewById(R.id.inbox_text_layout);

            TextView senderTextView = (TextView)messageView.findViewById(R.id.inbox_sender);
            senderTextView.setText(m.getAddress());

            TextView topicTextView = (TextView)messageView.findViewById(R.id.inbox_topic);
            topicTextView.setText(m.getSubject());

            TextView contentTextView = (TextView)messageView.findViewById(R.id.inbox_content);
            contentTextView.setText(m.getContent());

            TextView dateTextView = (TextView)messageView.findViewById(R.id.inbox_date);
            dateTextView.setText(m.getDate().toString());

            textViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearFragmentContent(leftBarParentLayout,contentParentLayout);
                    initMessageViewLayout(leftBarParentLayout, contentParentLayout, actualM, cm.getTrashMessageList());

                }
            });

            // Add the text view to the parent layout
            contentScrollLayout.addView(textViewLayout);
        }
    }


    private void initConceptsContent(LayoutInflater inflater,final LinearLayout contentParentLayout,final LinearLayout leftBarParentLayout){
        contentView = inflater.inflate(R.layout.sent_list_layout, contentParentLayout, false);

        LinearLayout contentScrollView = (LinearLayout)contentView.findViewById(R.id.sentScrollLayout);
        LinearLayout contentScrollLayout = (LinearLayout)contentView.findViewById(R.id.sent_list_layout);
        contentParentLayout.addView(contentScrollView);



        for (Message m : cm.getConceptMessageList()){
            final Message actualM = m;
            messageView = inflater.inflate(R.layout.single_inbox_layout, contentScrollLayout, false);
            LinearLayout textViewLayout = (LinearLayout)messageView.findViewById(R.id.inbox_text_layout);

            TextView senderTextView = (TextView)messageView.findViewById(R.id.inbox_sender);
            senderTextView.setText(m.getAddress());

            TextView topicTextView = (TextView)messageView.findViewById(R.id.inbox_topic);
            topicTextView.setText(m.getSubject());

            TextView contentTextView = (TextView)messageView.findViewById(R.id.inbox_content);
            contentTextView.setText(m.getContent());

            TextView dateTextView = (TextView)messageView.findViewById(R.id.inbox_date);
            dateTextView.setText(m.getDate().toString());

            textViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearFragmentContent(leftBarParentLayout,contentParentLayout);
                    initMessageViewLayout(leftBarParentLayout, contentParentLayout, actualM, cm.getTrashMessageList());

                }
            });
            // Add the text view to the parent layout
            contentScrollLayout.addView(textViewLayout);
        }
    }

    private void initMessageViewLayout(final LinearLayout leftBarParentLayout, final LinearLayout contentParentLayout,Message selectedMessage, List<Message> messageList) {

        Message actualMessage = selectedMessage;
        View leftPanelView = inflater.inflate(R.layout.message_left_panel, leftBarParentLayout, false);

        LinearLayout leftPanelLayout = (LinearLayout) leftPanelView.findViewById(R.id.left_panel_layout);
        LinearLayout leftPanelScrollViewContent = (LinearLayout) leftPanelView.findViewById(R.id.scroll_view_content);
        leftBarParentLayout.addView(leftPanelLayout);
        messageViewEnabled = true;

        View returnButtonView = inflater.inflate(R.layout.message_return_button,leftBarParentLayout,false);
        ImageButton returnButton = (ImageButton)returnButtonView.findViewById(R.id.message_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialView(leftBarParentLayout,contentParentLayout);
            }
        });

        leftPanelScrollViewContent.addView(returnButton);
        for (Message m : messageList) {
            final Message actualM = m;
            messageView = inflater.inflate(R.layout.message_overview_layout, leftPanelScrollViewContent, false);
            LinearLayout textViewLayout = (LinearLayout) messageView.findViewById(R.id.message_overview_layout);

            TextView senderTextView = (TextView) messageView.findViewById(R.id.inbox_sender);
            senderTextView.setText(m.getAddress());

            TextView topicTextView = (TextView) messageView.findViewById(R.id.inbox_topic);
            topicTextView.setText(m.getSubject());

            TextView dateTextView = (TextView) messageView.findViewById(R.id.inbox_date);
            dateTextView.setText(m.getDate().getDay() + "." + m.getDate().getMonth());

            textViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initMessageContent(contentParentLayout,actualM);
                }
            });
            // Add the text view to the parent layout
            leftPanelScrollViewContent.addView(textViewLayout);
        }

        initMessageContent(contentParentLayout,actualMessage);

    }



    private void initMessageContent(LinearLayout contentParentLayout, final Message selectedMessage){
        contentParentLayout.removeAllViews();
        View messageView = inflater.inflate(R.layout.message_layout, contentParentLayout, false);
        LinearLayout messageLayout = (LinearLayout)messageView.findViewById(R.id.message_layout);

        TextView dateView= (TextView)messageView.findViewById(R.id.date);
        dateView.setText(selectedMessage.getDate().toString());
        TextView senderView= (TextView)messageView.findViewById(R.id.sender);
        senderView.setText(selectedMessage.getAddress());
        TextView subjectView= (TextView)messageView.findViewById(R.id.subject);
        subjectView.setText(selectedMessage.getSubject());
        TextView contentView= (TextView)messageView.findViewById(R.id.content);
        contentView.setText(selectedMessage.getContent());


        Button replyButton = (Button)messageView.findViewById(R.id.reply_button);
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cm.setTempMessage(new Message(null,selectedMessage.getAddress(),selectedMessage.getSubject(),null,selectedMessage.getContent()));
                viewPager.setCurrentItem(2);
            }
        });
        Button resendButton = (Button)messageView.findViewById(R.id.resend_button);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cm.setTempMessage(new Message(null,"",selectedMessage.getSubject(),null,selectedMessage.getContent()));

                viewPager.setCurrentItem(2);

            }
        });
        contentParentLayout.addView(messageLayout);
    }
}
