package com.uta.mars

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.{MenuItem, Menu}
import com.github.clans.fab.FloatingActionButton
import com.uta.mars.common._
import org.scaloid.common._

class HomeAct extends SBaseActivity {

  private lazy val toolbar      = find[Toolbar](R.id.toolbar)
  private lazy val profileFAB   = find[FloatingActionButton](R.id.fab_profile)
  private lazy val clockInFAB   = find[FloatingActionButton](R.id.fab_clock_in)
  private lazy val clockOutFAB  = find[FloatingActionButton](R.id.fab_clock_out)
  private lazy val timeSheetFAB = find[FloatingActionButton](R.id.fab_time_sheet)
  private lazy val stopWatch    = find[StopWatchView](R.id.stop_watch)

  protected override def onCreate(b: Bundle): Unit = {
    super.onCreate(b)
    setContentView(R.layout.screen_home)
    setSupportActionBar(toolbar)
    setTitle("Home")

    Seq(profileFAB, clockInFAB, timeSheetFAB, clockOutFAB).foreach(_.hide(false))

    // Make those FABs animate in from a left to right sequence
    Seq(profileFAB, clockInFAB, timeSheetFAB).zip(1 to 3).foreach {
      case (fab, i) => delay(mills = i*250)(fab.show(true))
    }

    clockInFAB.onClick {
      stopWatch.reset()
      stopWatch.start()
      clockInFAB.hide(true)
      delay(mills = 250)(clockOutFAB.show(true))
    }

    clockOutFAB.onClick {
      stopWatch.stop()
      clockOutFAB.hide(true)
      delay(mills = 250)(clockInFAB.show(true))
    }
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.overflow, menu)
    true
  }

  override def onBackPressed(): Unit = {
    // Make it so the user can come back to this screen in the current state after they press
    // the back button to go to the android home screen. Without this code, the login screen
    // will be launched instead.
    val androidHomeScreen = new Intent(Intent.ACTION_MAIN)
    androidHomeScreen.addCategory(Intent.CATEGORY_HOME)
    androidHomeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(androidHomeScreen)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.menu_logout => finish(); startActivity[LoginAct]; true
      case _ /* no match */ => super.onOptionsItemSelected(item)
    }
  }
}
