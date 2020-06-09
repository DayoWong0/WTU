package grade.xyj.com.bean


data class DX(val topic: String, val option: String, val res: String)
data class PD(val topic: String, val res: String)
data class Work(val dx: List<DX>, val ddx: List<DX>, val pd: List<PD>)
data class All(val jds: Work, val mks: Work, val mg: Work, val sx: Work)