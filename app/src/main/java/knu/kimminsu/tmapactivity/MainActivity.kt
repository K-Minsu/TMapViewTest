package knu.kimminsu.tmapactivity

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.skt.Tmap.*

class MainActivity : AppCompatActivity(), TMapGpsManager.onLocationChangedCallback {
    var m_bTrackingMode : Boolean = true
    lateinit var tMapGpsManager : TMapGpsManager
    lateinit var tmapGps : TMapGpsManager
    lateinit var tmapview : TMapView
    val mApiKey : String = "l7xx6a347111bc9842009151e620e7301037"

    lateinit var tMapPolyLine: TMapPolyLine

    override fun onLocationChange(location: Location?) {
        if(m_bTrackingMode) {
            tmapview.setLocationPoint(location!!.longitude, location.latitude)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TMap Layout 설정
        var linearLayoutTmap = findViewById<LinearLayout>(R.id.linearlayoutTmap)
        tmapview = TMapView(this)
        linearLayoutTmap.addView(tmapview)

        // TMap Key 인증
        tmapview.setSKTMapApiKey(mApiKey)

        // 현재 보는 방향
        tmapview.setCompassMode(true)

        // 현 위치 아이콘 표시
        tmapview.setIconVisibility(true)

        // 기본 설정
        tmapview.zoomLevel = 15
        tmapview.mapType = TMapView.MAPTYPE_STANDARD
        tmapview.setLanguage(TMapView.LANGUAGE_KOREAN)

        // GPS 설정
        tmapGps = TMapGpsManager(this)
        tmapGps.minTime = 1000
        tmapGps.minDistance = 5f
        tmapGps.provider = TMapGpsManager.NETWORK_PROVIDER  // 인터넷으로 현 위치 받음
//        tmapGps.provider = TMapGpsManager.GPS_PROVIDER // GPS 위성으로 현 위치 받음
        tmapGps.OpenGps()

        // 화면 중심을 단말의 현재위치로 이동
        tmapview.setTrackingMode(true)
        tmapview.setSightVisible(true)

        // 경로 설정
        val tMapPointStart = TMapPoint(35.88688, 128.60850) // 공대 9호관(출발지)
        val tMapPointEnd = TMapPoint(35.89113011991111, 128.6119321645856) // 중앙도서관(목적지)

        // 경로 검색 (보행자)
        TMapData().findPathDataWithType(
            TMapData.TMapPathType.PEDESTRIAN_PATH, tMapPointStart, tMapPointEnd,
            TMapData.FindPathDataListenerCallback { polyLine ->
                polyLine.lineColor = Color.BLUE
                polyLine.lineWidth = 5f
                tmapview.addTMapPath(polyLine)
            })

        // 플로팅 버튼
        var fab_open = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_open)
        var fab_close = AnimationUtils.loadAnimation(applicationContext, R.anim.fab_close)

        var fab = findViewById<FloatingActionButton>(R.id.fab_btnMain)
        var fab1 = findViewById<FloatingActionButton>(R.id.fab_btn1)
        var fab2 = findViewById<FloatingActionButton>(R.id.fab_btn2)
        var fab3 = findViewById<FloatingActionButton>(R.id.fab_btn3)

        var FB = FloatingButton(fab_open, fab_close, fab, fab1, fab2, fab3)
    }
}