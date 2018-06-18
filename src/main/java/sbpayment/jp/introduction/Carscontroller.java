package sbpayment.jp.introduction;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Carscontroller {

	
	@GetMapping("/index")
	public String index(Model model) {
		return "index";
	}
	
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@GetMapping("/balance/{car_id}")//indexよりinsertページ遷移・車種別のID取得
	
	public String insert(@PathVariable("car_id") int type, Model model) {
		
		System.out.println("car_id:" + type);//車種別のID取得確認
		
		model.addAttribute("car_id", type);	
	
		
		

	return "balance";
	}
	
	

	@GetMapping("/cost")
	public String cost(Model model) {
	
			
		
		return "cost";
	
	}
	
	
	
	
	@GetMapping("/result")
	public String result(Model model) {
	
		return "result";
	
	}
	
	

	
	@PostMapping("/balance") //ユーザー収支情報取得 balance.html TABLE userspec
	public String balance(String user_name,double income, double rent, double utility_c, double commu_c,int car_id, RedirectAttributes attr){
	    
		
		System.out.println("checked PM1");//確認用
		
		
		attr.addFlashAttribute("user_name", user_name);	
		attr.addFlashAttribute("income", income);
	    attr.addFlashAttribute("rent", rent);
	    attr.addFlashAttribute("utility_c", utility_c);
	    attr.addFlashAttribute("commu_c", commu_c);
	    attr.addFlashAttribute("car_id", car_id);
	    
	    
	    //確認用2
		System.out.println(user_name);
	    System.out.println(income);
	    System.out.println(rent);
	    System.out.println(utility_c);
	    System.out.println(commu_c);
	    System.out.println("car_id:" + car_id);
	    
	    
		jdbc.update("INSERT INTO userspec (user_name,income,rent,utility_c,commu_c) values(?,?,?,?,?);",
				user_name,income,rent,utility_c,commu_c); //DB userspec TABLEに格納
		
		//jdbc.update("UPDATE userspec SET income = ?,rent = ?, utility_c = ? , commu_c = ?", 
			//	income, rent, utility_c, commu_c);
	
		//attr.addFlashAttribute("users",jdbc.queryForMap("SELECT * FROM userspec"));
	    // where でユーザーid指定 1レコードを持ってきたいから queryMap
	    
	    
		return "redirect:/cost";
		 
	}
	
	@PostMapping("/cost") //ユーザー情報取得2 cost.html TABLE user_cost
	public String cost(String user_name,double parking, double running, int loan,int car_id,RedirectAttributes attr){
	    
		System.out.println("checked PM2");//確認用
		
		System.out.println(user_name);
		
		attr.addFlashAttribute("car_id", car_id);
		attr.addFlashAttribute("parking", parking);
	    attr.addFlashAttribute("running", running);
	    attr.addFlashAttribute("loan", loan);

	    //確認用2
	    System.out.println(parking);
	    System.out.println(running);
	    System.out.println(loan);
	    System.out.println("car_id:" + car_id);

	    jdbc.update("UPDATE userspec SET parking = ?,running = ?, loan = ?  ", parking,running,loan);
	    
	    //jdbc.update("UPDATE userspec SET parking = ?,running = ?, loan = ? WHERE user_name = user_ name ", parking,running,loan);
	    //user_name　レコードと対応させる
	    
		return "redirect:/calculation";
	}
	
	
	@GetMapping("/calculation")
	public String calculation(Model model) {
	
		return "calculation";
	
	}
	
	
	
	
	
	@PostMapping("/calculation") //計算用
	public String calculation(String user_name,int car_id,RedirectAttributes attr){
	    
		attr.addFlashAttribute("car_id", car_id);
		System.out.println("checked calculation");//確認用
		
		System.out.println("car_id" + car_id);
			
		
		//DBより車両情報取得
		List<Map<String, Object>>car_name = jdbc.queryForList("SELECT name FROM cars WHERE car_id = ? ", car_id);//車両名
		Map<String, Object>car_tax = jdbc.queryForList("SELECT car_tax FROM cars WHERE car_id = ? ", car_id).get(0);//自動車税
		Map<String, Object>weight_tax = jdbc.queryForList("SELECT weight_tax FROM cars WHERE car_id = ? ", car_id).get(0);//重量税
		Map<String, Object>liability_ins = jdbc.queryForList("SELECT liability_ins FROM cars WHERE car_id = ? ", car_id).get(0);//自賠責保険
		Map<String, Object>voluntary_ins = jdbc.queryForList("SELECT voluntary_ins FROM cars WHERE car_id = ? ", car_id).get(0);//任意保険
		Map<String, Object>month_total = jdbc.queryForList("SELECT month_total FROM cars WHERE car_id = ? ", car_id).get(0);//月間固定費用
		Map<String, Object>f_type = jdbc.queryForList("SELECT f_type FROM cars WHERE car_id = ? ", car_id).get(0);//油種
		Map<String, Object>car_price = jdbc.queryForList("SELECT price FROM price WHERE car_id = ? ", car_id).get(0);//車両価格
		Map<String, Object>fuel = jdbc.queryForList("SELECT fuel_ec FROM fuel WHERE car_id = ? ", car_id).get(0);//燃費
		//List<Map<String, Object>>fuel = jdbc.queryForList("SELECT fuel_ec FROM fuel WHERE car_id = ? ", car_id);//燃費
		
	
		System.out.println(car_name);
		System.out.println(car_tax);
		System.out.println(weight_tax);
		System.out.println(liability_ins);
		System.out.println(voluntary_ins);
		System.out.println(month_total);
		System.out.println(f_type);
		System.out.println(car_price);
		System.out.println(fuel);
		
		

		
		
		//double型に変換
		double car_t = Double.valueOf(car_tax.get("car_tax").toString());//自動車税
		System.out.println("test" + car_t);
		
		double weight_t = Double.valueOf(weight_tax.get("weight_tax").toString());//重量税
		System.out.println("test" + weight_t);
		
		double liability_i = Double.valueOf(liability_ins.get("liability_ins").toString());//月間固定費
		System.out.println("test" + liability_i);
		
		double car_p = Double.valueOf(car_price.get("price").toString());//車両価格
		System.out.println("test" + car_p);
		
		double fuel_e = Double.valueOf(fuel.get("fuel_ec").toString());//燃費
		System.out.println("test" + fuel_e);
		
		
		//DBよりユーザー情報取得 ※useridと対応させる
		Map<String, Object>income = jdbc.queryForList("SELECT income FROM userspec").get(0);//月収
		Map<String, Object>rent = jdbc.queryForList("SELECT rent FROM userspec").get(0);//家賃
		Map<String, Object>utility_c = jdbc.queryForList("SELECT utility_c FROM userspec").get(0);//光熱費
		Map<String, Object>commu_c = jdbc.queryForList("SELECT commu_c FROM userspec").get(0);//通信費
		Map<String, Object>parking = jdbc.queryForList("SELECT parking FROM userspec").get(0);//駐車場代
		Map<String, Object>running = jdbc.queryForList("SELECT running FROM userspec").get(0);//走行距離
		Map<String, Object>loan = jdbc.queryForList("SELECT loan FROM userspec").get(0);
		
		//double型に変換
		double income_e = Double.valueOf(income.get("income").toString());//月収
		System.out.println("test" + income_e);
		double rent_e = Double.valueOf(rent.get("rent").toString());//家賃
		System.out.println("test" + rent_e);
		double utility_e = Double.valueOf(utility_c.get("utility_c").toString());//光熱費
		System.out.println("test" + utility_e);
		double commu_e= Double.valueOf(commu_c.get("commu_c").toString());//通信費
		System.out.println("test" + commu_e);
		double parking_e= Double.valueOf(parking.get("parking").toString());//駐車場代
		System.out.println("test" + parking_e);
		double running_e = Double.valueOf(running.get("running").toString());//走行距離
		System.out.println("test" + running_e);
		double loan_e = Double.valueOf(loan.get("loan").toString());//ローン支払い回数
		System.out.println("test" + loan_e);
		
		System.out.println("車種は" + car_name);
		
		//維持費計算
		double repayment;//月々のローン返済額
		repayment = car_p / loan_e;
		System.out.println("月々のローン返済額　" + repayment+ "万円");
		
		double gas;//月々のガソリン代
		double gas_price;//ガソリン価格/l
		gas_price = 0.0145;//ガソリン価格を145円と仮定
		gas = gas_price * running_e;
		System.out.println("月々のガソリン代" + gas+ "円");
		
		double totalcost;//月々の維持費合計
		totalcost = month_t + repayment + gas + parking_e;
		System.out.println("車に掛かるお金は　" + totalcost + "万円");
		
		
		//家計計算
		double budget;
		budget = income_e - (rent_e + utility_e + commu_e);
		System.out.println("車に使えるお金は　" + budget+ "万円");
		
		
//		
//		jdbc.update("INSERT INTO result (car_name,car_price,month_total,fuel_ec,income,rent,utility_e,"
//				+ "comm_c,parking,running,loan,repayment.gas,totalcost.budget) "
//				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
//				car_name,car_price,rent,utility_c,commu_c);
		
		
		// where でユーザーid指定 1レコードを持ってきたいから queryMap
	    ////Map<String, Object> person = jdbc.queryForMap("SELECT * FROM userspec where name = ?", name).get(0);

	    
		return "redirect:/purchasable";
		 
	}
	
	
	@GetMapping("/purchasable")
	public String purchasable(Model model) {
	
//		model.addAttribute("car_id", car_id);	
		//System.out.println("car_id;　" + car_id);
		
		return "purchasable";
	
	}
	
}
	
	


//"redirect:/calculation";






