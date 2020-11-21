package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.R

class DiaryAdapter : RecyclerView.Adapter<DiaryAdapter.DiaryHolder>() {

    private var listener: OnItemClickListener? = null
    private var diaries: List<Diary> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryHolder {
        var itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.diary_item, parent, false
        )
        return DiaryHolder(itemView)
    }

    override fun onBindViewHolder(holder: DiaryHolder, position: Int) {
        var currentDiary: Diary = diaries[position]
        holder.textViewTitle.text = (currentDiary.title)
        holder.textViewDescription.text = (currentDiary.description)
        holder.textViewLocation.text = (currentDiary.location)
    }

    override fun getItemCount(): Int {
        return diaries.size
    }

    fun setDiaries(diary: List<Diary>) {
        diaries = diary
        notifyDataSetChanged()
    }

    fun getDiaryAt(position: Int): Diary {
        return diaries[position]
    }

    inner class DiaryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView
        var textViewDescription: TextView
        var textViewLocation: TextView

        init {
            textViewTitle = itemView.findViewById(R.id.text_view_title)
            textViewDescription = itemView.findViewById(R.id.text_view_description)
            textViewLocation = itemView.findViewById(R.id.text_view_location)

            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(diaries.get(position))
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(diary: Diary?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }
}
