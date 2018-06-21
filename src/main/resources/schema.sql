 /* 車の情報を記載するテーブル作成*/ 
 /*liability_ins = 自賠責, voluntary_ins = 任意保険*/
 CREATE TABLE cars(
 	car_id INT, name VARCHAR(100), car_tax FLOAT, weight_tax FLOAT, liability_ins FLOAT,
 	voluntary_ins FLOAT, month_total FLOAT,f_type VARCHAR(100)
 	);
 /*月間固定維持費*/
 INSERT INTO cars VALUES
 	(1, 'SUBARU WRX STI', 0.33, 0.13, 0.13, 2.2, 2.79, 'hi'),(2, 'NISSAN GT-R', 0.56, 0.14, 0.13, 2.43, 3.26, 'hi'), (3, 'SUZUKI SwiftSports', 0.29, 0.07, 0.13, 1.92, 2.41, 'hi'),
 	(4, 'DAIHATSU Copen',  0.08, 0.02, 0.13, 1.25, 1.48, 're'), (5, 'TOYOTA Aqua', 0.29, 0.07, 0.13, 1.25, 2.58, 're'), (6, 'SUZUKI Hustler', 0.08, 0.02, 0.13, 1.33, 1.56, 're'),
 	(7, 'LEXUS LX', 0.73, 0.13, 0.13, 3.17, 4.16, 'hi'), (8, 'Mercedes-benz S550', 0.73, 0.08, 0.13, 3.25, 4.20, 're'), (9,'TOYOTA Alphard', 0.38, 0.08, 0.13, 2.33, 2.93, 're'),
 	(10, 'BMW 318i', 0.29, 0.08, 0.13, 2.25, 2.76,'hi'), (11, 'TOYOTA Prius', 0.33, 0.06, 0.13, 1.92, 2.44, 're'), (12, 'NISSAN X-trail', 0.33, 0.08, 0.13, 2.17, 2.71, 're');
 	
 	
/* 	価格表*/
 CREATE TABLE price(
 	car_id INT, price FLOAT  
 	); 	
 /*価格（万円）*/
 INSERT INTO price VALUES
 	(1, 450.0), (2, 1170.0), (3, 183.0), (4, 187.0), (5, 188.0), (6, 135.0),
 	(7, 1115.0), (8, 1360.0), (9,370.0), (10, 457.0), (11, 278.0), (12, 240.0);
 
 /*燃費表*/
 CREATE TABLE fuel(
 	car_id INT, fuel_ec FLOAT  
 	); 
 
 /*(km/l)*/
 INSERT INTO fuel VALUES
 	(1, 9.4), (2, 8.7), (3, 16.4), (4, 22.2), (5, 34.4), (6, 33.4),
 	(7,  6.5), (8, 6.7), (9, 11.6), (10, 13.2), (11,37.2), (12, 16.0);
 
 

 	 	
/* 	ユーザー情報入力用テーブル*/
　CREATE TABLE userspec(
 	user_name VARCHAR(200), income FLOAT,rent FLOAT, utility_c FLOAT, commu_c FLOAT, expense_c FLOAT, budget FLOAT,
 	parking FLOAT,running FLOAT,deposit FLOAT, loan FLOAT);
 	
 	
 	
/*結果格納テーブル　入力内容と計算結果をDBへ再び格納*/
　CREATE TABLE result(
 	car_id int, car_tax FLOAT, weight_tax FLOAT, liability_ins FLOAT, voluntary_ins FLOAT, month_total FLOAT, f_type FLOAT,  price FLOAT,fuel_ec FLOAT, income FLOAT, rent FLOAT, utility_c FLOAT, comm_c FLOAT,parking FLOAT, running FLOAT, loan FLOAT,
 	repayment FLOAT, gas FLOAT, totalcost FLOAT, budget FLOAT);
 		
 
/* 	ユーザー情報入力用テーブル2*/
/*　CREATE TABLE user_cost(
 	user_id INT, parking FLOAT,running FLOAT, loan INT,
 	PRIMARY KEY(user_id));*/
 	 
 /* 
CREATE TABLE userspec(
 	income FLOAT,rent FLOAT, utility_c FLOAT, commu_c FLOAT, budget FLOAT);*/