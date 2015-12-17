package com.rndapp.roostremote.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.models.Endpoint;

import java.util.List;

/**
 * Created by ell on 4/29/15.
 */
public class EndpointAdapter extends RecyclerView.Adapter<EndpointAdapter.EndpointViewHolder>{
    Context context;
    List<Endpoint> endpoints;
    RequestQueue queue;

    public EndpointAdapter(Context context, List<Endpoint> endpoints) {
        this.context = context;
        this.endpoints = endpoints;
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    public EndpointViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.card_view, viewGroup, false);
        return new EndpointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EndpointViewHolder endpointViewHolder, int i) {
        endpointViewHolder.setEndpoint(endpoints.get(i));
    }

    @Override
    public int getItemCount() {
        return endpoints.size();
    }

    public class EndpointViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTextView;
        TextView endpointTextView;
        Endpoint endpoint;

        public EndpointViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            endpointTextView = (TextView) itemView.findViewById(R.id.tv_endpoint);
            itemView.setOnClickListener(this);
        }

        public void setEndpoint(Endpoint endpoint){
            this.endpoint = endpoint;
            nameTextView.setText(endpoint.getName());
            endpointTextView.setText(endpoint.getEndpoint());
        }

        @Override
        public void onClick(View v) {
            //show chooser

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    context);
            builder.setTitle("Choose an Action");
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            final ActionAdapter adapter = new ActionAdapter(context, endpoint.getOptionsHolder().getValues());
            builder.setAdapter(adapter,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            endpoint.execute(queue, endpoint.getOptionsHolder().getValues().get(which));
                        }
                    });
            builder.show();
        }
    }
}
