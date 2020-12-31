package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder


class CountDownTimerService : Service() {
    var Count: CountDownTimer? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Count = object : CountDownTimer(TIME_LIMIT, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //coundownTimer.setTitle(millisUntilFinished / 1000);
            }

            override fun onFinish() {
                //coundownTimer.setTitle("Sedned!");
                val i = Intent("COUNTDOWN_UPDATED")
                i.putExtra("countdown", "FINISH")
                sendBroadcast(i)
                //Log.d("COUNTDOWN", "FINISH!");
                stopSelf()
            }
        }
        (Count as CountDownTimer).start()
        return START_NOT_STICKY
    }

    override fun onBind(arg0: Intent?): IBinder? {
        // TODO Auto-generated method stub
        return null
    }

    override fun onDestroy() {
        Count!!.cancel()
        super.onDestroy()
    }

    companion object {
        var TIME_LIMIT: Long = 10000
    }
}