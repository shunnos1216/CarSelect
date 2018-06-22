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

	@GetMapping("/balance/{car_id}") // indexよりinsertページ遷移・車種別のID取得

	public String insert(@PathVariable("car_id") int type, Model model) {

		System.out.println("car_id:" + type);// 車種別のID取得確認

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

	@PostMapping("/balance") // ユーザー収支情報取得 balance.html TABLE userspec
	public String balance(String user_name, double income, double rent, double utility_c, double commu_c, int car_id,
			double expense_c, RedirectAttributes attr) {

		System.out.println("checked PM1");// 確認用

		attr.addFlashAttribute("user_name", user_name);
		attr.addFlashAttribute("income", income);
		attr.addFlashAttribute("rent", rent);
		attr.addFlashAttribute("utility_c", utility_c);
		attr.addFlashAttribute("commu_c", commu_c);
		attr.addFlashAttribute("car_id", car_id);
		attr.addFlashAttribute("car_id", car_id);
		attr.addFlashAttribute("expense_c", expense_c);

		// 確認用2
		System.out.println(user_name);
		System.out.println(income);
		System.out.println(rent);
		System.out.println(utility_c);
		System.out.println(commu_c);
		System.out.println(expense_c);
		System.out.println("car_id:" + car_id);

		jdbc.update("INSERT INTO userspec (user_name,income,rent,utility_c,commu_c,expense_c) values(?,?,?,?,?,?);",
				user_name, income, rent, utility_c, commu_c, expense_c); // DB userspec TABLEに格納

		// jdbc.update("UPDATE userspec SET income = ?,rent = ?, utility_c = ? , commu_c
		// = ?",
		// income, rent, utility_c, commu_c);

		// attr.addFlashAttribute("users",jdbc.queryForMap("SELECT * FROM userspec"));
		// where でユーザーid指定 1レコードを持ってきたいから queryMap

		return "redirect:/cost";

	}

	@PostMapping("/cost") // ユーザー情報取得2 cost.html TABLE user_cost
	public String cost(String user_name, double parking, double running, double deposit, int loan, int car_id,
			RedirectAttributes attr) {

		System.out.println("checked PM2");// 確認用

		System.out.println(user_name);

		// DBから車両価格取得
		Map<String, Object> car_price = jdbc.queryForList("SELECT price FROM price WHERE car_id = ? ", car_id).get(0);// 車両価格
		double car_p = Double.valueOf(car_price.get("price").toString());// 車両価格
		System.out.println("test車両価格" + car_p);

		attr.addFlashAttribute("car_p", car_p);

		attr.addFlashAttribute("car_id", car_id);
		attr.addFlashAttribute("parking", parking);
		attr.addFlashAttribute("running", running);
		attr.addFlashAttribute("loan", loan);
		attr.addFlashAttribute("deposit", deposit);

		// 確認用2
		System.out.println(parking);
		System.out.println(running);
		System.out.println(loan);
		System.out.println(deposit);
		System.out.println("car_id:" + car_id);

		jdbc.update("UPDATE userspec SET parking = ?,running = ?, deposit = ?, loan = ? ", parking, running, deposit,
				loan);

		// jdbc.update("UPDATE userspec SET parking = ?,running = ?, loan = ? WHERE
		// user_name = user_ name ", parking,running,loan);
		// user_name レコードと対応させる

		return "redirect:/calculation";
	}

	@GetMapping("/calculation")
	public String calculation(Model model) {

		return "calculation";

	}

	@PostMapping("/calculation") // 計算用
	public String calculation(String user_name, int car_id, RedirectAttributes attr) {

		attr.addFlashAttribute("car_id", car_id);
		System.out.println("checked calculation");// 確認用

		System.out.println("car_id" + car_id);

		// DBより車両情報取得
		// Map<String,Object> = map SELECT * FROM table WHRE id = ?,id
		// 以下の情報は Map<Stiring, object> = jdbc.queryForList("SELECT name FROM cars WHERE
		// car_id = ? ", car_id)で一行で持ってこれる

		// Map<String, Object>cars = jdbc.queryForList("SELECT * FROM cars WHERE car_id
		// = ? ", car_id).get(0);

		Map<String, Object> car_name = jdbc.queryForList("SELECT name FROM cars WHERE car_id = ? ", car_id).get(0);// 車両名
		Map<String, Object> car_tax = jdbc.queryForList("SELECT car_tax FROM cars WHERE car_id = ? ", car_id).get(0);// 自動車税
		Map<String, Object> weight_tax = jdbc.queryForList("SELECT weight_tax FROM cars WHERE car_id = ? ", car_id)
				.get(0);// 重量税
		Map<String, Object> liability_ins = jdbc
				.queryForList("SELECT liability_ins FROM cars WHERE car_id = ? ", car_id).get(0);// 自賠責保険
		Map<String, Object> voluntary_ins = jdbc
				.queryForList("SELECT voluntary_ins FROM cars WHERE car_id = ? ", car_id).get(0);// 任意保険
		Map<String, Object> month_total = jdbc.queryForList("SELECT month_total FROM cars WHERE car_id = ? ", car_id)
				.get(0);// 月間固定費用
		// Map<String, Object>f_type = jdbc.queryForList("SELECT f_type FROM cars WHERE
		// car_id = ? ", car_id).get(0);//油種
		Map<String, Object> car_price = jdbc.queryForList("SELECT price FROM price WHERE car_id = ? ", car_id).get(0);// 車両価格
		Map<String, Object> fuel = jdbc.queryForList("SELECT fuel_ec FROM fuel WHERE car_id = ? ", car_id).get(0);// 燃費

		// List<Map<String, Object>>fuel = jdbc.queryForList("SELECT fuel_ec FROM fuel
		// WHERE car_id = ? ", car_id);//燃費
		// List<Map<String, Object>>car_name = jdbc.queryForList("SELECT name FROM cars
		// WHERE car_id = ? ", car_id);//車両名

		System.out.println(car_name.get("name"));
		System.out.println(car_tax);
		System.out.println(weight_tax);
		System.out.println(liability_ins);
		System.out.println(voluntary_ins);
		System.out.println(month_total);
		// System.out.println(f_type);
		System.out.println(car_price);
		System.out.println(fuel);

		// double型に変換
		// double car_n = Double.valueOf(car_name.get("name").toString());//車種名
		// System.out.println("test" + car_n);

		double car_t = Double.valueOf(car_tax.get("car_tax").toString());// 自動車税
		System.out.println("test自動車税" + car_t);

		double weight_t = Double.valueOf(weight_tax.get("weight_tax").toString());// 重量税
		System.out.println("test重量税" + weight_t);

		double liability_i = Double.valueOf(liability_ins.get("liability_ins").toString());// 自賠保険
		System.out.println("test自賠責保険" + liability_i);

		double voluntary_i = Double.valueOf(voluntary_ins.get("voluntary_ins").toString());// 任意保険
		System.out.println("test任意保険" + voluntary_i);

		double month_t = Double.valueOf(month_total.get("month_total").toString());// 月間固定費
		System.out.println("test月間固定費" + month_t);

		// Integer fuel_t = Integer.valueOf(f_type.get("f_type").toString());//油種
		// System.out.println("test油種" + fuel_t);

		double car_p = Double.valueOf(car_price.get("price").toString());// 車両価格
		System.out.println("test車両価格" + car_p);

		double fuel_e = Double.valueOf(fuel.get("fuel_ec").toString());// 燃費
		System.out.println("test燃費" + fuel_e);

		// DBよりユーザー情報取得 ※useridと対応させる
		Map<String, Object> income = jdbc.queryForList("SELECT income FROM userspec").get(0);// 月収
		Map<String, Object> rent = jdbc.queryForList("SELECT rent FROM userspec").get(0);// 家賃
		Map<String, Object> utility_c = jdbc.queryForList("SELECT utility_c FROM userspec").get(0);// 光熱費
		Map<String, Object> commu_c = jdbc.queryForList("SELECT commu_c FROM userspec").get(0);// 通信費
		Map<String, Object> expense_c = jdbc.queryForList("SELECT expense_c FROM userspec").get(0);// 通信費
		Map<String, Object> parking = jdbc.queryForList("SELECT parking FROM userspec").get(0);// 駐車場代
		Map<String, Object> running = jdbc.queryForList("SELECT running FROM userspec").get(0);// 走行距離
		Map<String, Object> deposit = jdbc.queryForList("SELECT deposit FROM userspec").get(0);
		Map<String, Object> loan = jdbc.queryForList("SELECT loan FROM userspec").get(0);// ローン支払い回数

		// double型に変換
		double income_e = Double.valueOf(income.get("income").toString());// 月収
		System.out.println("test月収" + income_e);
		double rent_e = Double.valueOf(rent.get("rent").toString());// 家賃
		System.out.println("test家賃" + rent_e);
		double utility_e = Double.valueOf(utility_c.get("utility_c").toString());// 光熱費
		System.out.println("test光熱費" + utility_e);
		double commu_e = Double.valueOf(commu_c.get("commu_c").toString());// 通信費
		System.out.println("test通信費" + commu_e);
		double expense_e = Double.valueOf(expense_c.get("expense_c").toString());// 交際費
		System.out.println("test交際費" + commu_e);
		double parking_e = Double.valueOf(parking.get("parking").toString());// 駐車場代
		System.out.println("test駐車場代 " + parking_e);
		double running_e = Double.valueOf(running.get("running").toString());// 走行距離
		System.out.println("test走行距離" + running_e);
		double deposit_e = Double.valueOf(deposit.get("deposit").toString());// 頭金
		System.out.println("test頭金" + deposit);
		double loan_e = Double.valueOf(loan.get("loan").toString());// ローン支払い回数
		System.out.println("testローン支払い回数" + loan_e);

		System.out.println("車種: " + car_name);
		System.out.println("車体価格: " + car_p);
		System.out.println("頭金: " + deposit_e);
		System.out.println("ローン支払い回数: " + loan_e);

		double repayment;// 月々のローン返済額
		repayment = (car_p - deposit_e) / loan_e;
		System.out.println("月々のローン返済額:　" + repayment + "万円");

		double gas;// 月々のガソリン代
		double gas_price;// ガソリン価格/l

		gas_price = 0.015;
		// if (f_type = 1) {
		// gas_price = 155;
		// }else {
		// gas_price = 145;
		// }

		String.format("%.2f", gas = running_e / fuel_e * gas_price);
		System.out.println("月々のガソリン代: " + gas + "円");

		double totalcost;// 月々の維持費合計
		String.format("%.2f", totalcost = month_t + repayment + gas + parking_e);
		System.out.println("車に掛かるお金:　" + String.format("%.2f", totalcost) + "万円");

		// 家計計算
		double budget;
		budget = income_e - (rent_e + utility_e + commu_e + expense_e);
		System.out.println("車に使えるお金:　" + budget + "万円");

		// 差額計算
		double difference;
		difference = budget - totalcost;
		System.out.println("差額:　" + difference + "万円");

		// String url =
		// ("https://www.carsensor.net/usedcar/search.php?SKIND=1&fed=toppcfws20150707001ta&KW=")
		// + car_name.get("name");

		String url = ("https://www.carsensor.net/usedcar/freeword/") + car_name.get("name") + ("/index.html?SORT=2");

		System.out.println(url);

		System.out.println("-------------------以下判定--------------------");

		
		
		// 購入・維持ができるか判定
		if (budget > totalcost) {

			System.out.println("購入可能");
			System.out.println("車に使えるお金(" + budget + "万円) >" + "維持費(" + totalcost + "万円)");

			System.out.println(car_name);
			System.out.println(totalcost);
			System.out.println("差額:　" + String.format("%.2f", difference) + "万円");

			attr.addFlashAttribute("car_name", car_name.get("name"));
			attr.addFlashAttribute("budget", budget);
			// attr.addFlashAttribute("ｔotalcost", String.format("%.2f",totalcost));
			attr.addFlashAttribute("totalcost", String.format("%.2f", totalcost));
			attr.addFlashAttribute("car_t", car_t);
			attr.addFlashAttribute("weight_t", weight_t);
			attr.addFlashAttribute("liability_i", liability_i);
			attr.addFlashAttribute("voluntary_i", voluntary_i);
			attr.addFlashAttribute("month_t", month_t);
			attr.addFlashAttribute("car_p", car_p);
			attr.addFlashAttribute("loan_e", loan_e);
			attr.addFlashAttribute("repayment", String.format("%.2f", repayment));
			attr.addFlashAttribute("parking_e", parking_e);
			attr.addFlashAttribute("gas", String.format("%.2f", gas));//四捨五入
			attr.addFlashAttribute("difference", String.format("%.2f", difference));
			attr.addFlashAttribute("url", url);

			return "redirect:/purchasable";
		} else {

			System.out.println("購入不可");
			System.out.println("車に使えるお金(" + budget + "万円) <" + "維持費(" + totalcost + "万円)");

			System.out.println(car_name);

			System.out.println(totalcost);

			difference = difference * -1;
			System.out.println("差額:　" + String.format("%.2f", difference) + "万円");
			attr.addFlashAttribute("car_name", car_name.get("name"));
			attr.addFlashAttribute("budget", budget);
			attr.addFlashAttribute("totalcost", String.format("%.2f", totalcost));
			attr.addFlashAttribute("car_t", car_t);
			attr.addFlashAttribute("weight_t", weight_t);
			attr.addFlashAttribute("liability_i", liability_i);
			attr.addFlashAttribute("voluntary_i", voluntary_i);
			attr.addFlashAttribute("month_t", month_t);
			attr.addFlashAttribute("car_p", car_p);
			attr.addFlashAttribute("loan_e", loan_e);
			attr.addFlashAttribute("repayment", String.format("%.2f", repayment));
			attr.addFlashAttribute("parking_e", parking_e);
			attr.addFlashAttribute("gas", String.format("%.2f", gas));
			attr.addFlashAttribute("difference", String.format("%.2f", difference));
			attr.addFlashAttribute("url", url);

			return "redirect:/unpurchasable";

		}

	}

	@GetMapping("/purchasable")
	public String purchasable(Model model) {

		return "purchasable";

	}

	@GetMapping("/unpurchasable")
	public String unpurchasable(Model model) {

		return "unpurchasable";

	}

	// 車種・ユーザー情報・計算結果をDB resultテーブルに格納1
	// jdbc.update("INSERT INTO result
	// (car_id,car_tax,weight_tax,liability_ins,voluntary_ins,month_total,f_type,price,fuel_ec,"
	// +
	// "income,rent,utility_c,comm_c,parking,running,loan,repayment,gas,totalcost,budget)
	// "
	// + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
	// car_id,car_t,weight_t,liability_i,voluntary_i,month_t,fuel_t,car_p,fuel_e,income_e,rent_e,utility_e,commu_e,parking_e,
	// running_e,loan_e,repayment,gas,totalcost,budget);

	// 種・ユーザー情報・計算結果をDB resultテーブルに格納2
	// jdbc.update("INSERT INTO result
	// (car_id,price,fuel_ec,income,rent,utility_c,comm_c,parking,running,loan,repayment,gas,totalcost,budget)
	// "
	// + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
	// car_id,car_p,fuel_e,income_e,rent_e,utility_e,commu_e,parking_e,running_e,loan_e,repayment,gas,totalcost,budget);

	// jdbc.queryForList("SELECT * FROM result INNER JOIN cars ON result.car_id =
	// cars.car_id WHERE result.car_id = ?",car_id);

	// where でユーザーid指定 1レコードを持ってきたいから queryMap
	//// Map<String, Object> person = jdbc.queryForMap("SELECT * FROM userspec where
	// name = ?", name).get(0);

	// attr.addAttribute("peoples",jdbc.queryForList("SELECT * FROM result"));

	// return "redirect:/purchasable";
	//
	// }

	// @GetMapping("/purchasable")
	// public String purchasable(Model model) {
	//
	//
	//
	// return "purchasable";
	//
	// }

}

// "redirect:/calculation";
