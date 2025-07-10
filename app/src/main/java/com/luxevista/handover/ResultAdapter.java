package com.luxevista.handover;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private List<List<String>> data;
    private List<String> headers;

    // Only display these columns
    private final List<String> requiredHeaders = Arrays.asList(
            "ID", "ACCT_NUM", "MET_NUM", "WALK_ORDER", "NAME", "ADDRESS",
            "BILL CYCLE", "TARIFF_COD", "DATE", "COM", "ROUTE", "TP"
    );

    public ResultAdapter(List<List<String>> data) {
        this.data = data;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void updateData(List<List<String>> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ResultAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ViewHolder holder, int position) {
        List<String> row = data.get(position);
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < headers.size() && i < row.size(); i++) {
            String header = headers.get(i).trim();
            if (requiredHeaders.contains(header)) {
                String cleanHeader = header.replace("_", " ").replace("TARIFF_COD", "TARIFF COD");
                String raw = (row.get(i) != null) ? row.get(i).trim() : "";
                String value = raw.isEmpty() ? "–" : raw;

                formatted.append("<b>")
                        .append(cleanHeader)
                        .append("</b>: ")
                        .append(value)
                        .append("<br>");
            }
        }

        holder.textView.setText(Html.fromHtml(formatted.toString().trim(), Html.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.rowText);
        }
    }
}