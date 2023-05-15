package services

import requests.Response

import javax.inject.Singleton

@Singleton
class HttpDispatcher() {

  def sendRequest(secid: String): String = {
    val r: Response = requests.get("https://iss.moex.com/iss/securities.xml?q=" + secid)
    r.data.toString()
  }
}
