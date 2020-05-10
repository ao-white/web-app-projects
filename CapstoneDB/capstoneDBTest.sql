drop database if exists capstone_test;

create database capstone_test;

use capstone_test;

create table user_account (
	user_id int primary key auto_increment,
    user_name varchar(30) not null unique,
    `password` varchar(30) not null unique,
    description varchar(255),
    picture_url varchar(255),
    favorite_park varchar(50),
    role varchar(30) default 'ROLE_USER'
);

create table park (
	park_id varchar(4) primary key,
    `name` varchar(50) not null
);

create table park_visit (
	visit_id int primary key auto_increment,
    start_date date not null,
    end_date date not null,
    user_id int not null,
    park_id varchar(4) not null,
    constraint fk_park_visit_user_account
	  foreign key (user_id)
      references user_account(user_id),
	constraint fk_park_visit_park
	  foreign key (park_id)
      references park(park_id)
);

create table log_entry (
	entry_id int primary key auto_increment,
    entry_date date not null,
    entry varchar(500) not null,
    visit_id int not null,
	constraint fk_log_entry_park_visit
	  foreign key (visit_id)
      references park_visit(visit_id)
);

create table picture (
	picture_id int primary key auto_increment,
    picture_url varchar(255) not null,
    title varchar(50) not null,
    picture_date date not null,
    visit_id int not null,
    constraint fk_picture_park_visit
	  foreign key (visit_id)
      references park_visit(visit_id)
 );
 
 insert into user_account(user_name, `password`, description, picture_url, favorite_park)
	values
('user', 'password', 'This is a description of a test user.', 'images/gnome.jpg', 'Big Bend');
 
insert into park(park_id, `name`)
	values  
('ACAD',  'Acadia'),
('ARCH',  'Arches'),
('BADL',  'Badlands'),
('BIBE',  'Big Bend'),
('BISC',  'Biscayne'),
('BLCA',  'Black Canyon of the Gunnison'),
('BRCA',  'Bryce Canyon'),
('CANY',  'Canyonlands'),
('CARE',  'Capitol Reef'),
('CAVE',  'Carlsbad Caverns'),
('CHIS',  'Channel Islands'),
('CONG',  'Congaree'),
('CRLA',  'Crater Lake'),
('CUVA',  'Cuyahoga Valley'),
('DEVA',  'Death Valley'),
('DEWA',  'Denali'),
('DRTO',  'Dry Tortugas'),
('EVER',  'Everglades'),
('GAAR',  'Gates of the Arctic'),
('JEFF',  'Gateway Arch'),
('GLBA',  'Glacier Bay'),
('GLAC',  'Glacier'),
('GRCA',  'Grand Canyon'),
('GRTE',  'Grand Teton'),
('GRBA',  'Great Basin'),
('GRSA',  'Great Sand Dunes'),
('GRSM',  'Great Smoky Mountains'),
('GUMO',  'Guadalupe Mountains'),
('HALE',  'Haleakala'),
('HAVO',  'Hawaii Volcanoes'),
('HOSP',  'Hot Springs'),
('INDU',  'Indiana Dunes'),
('ISRO',  'Isle Royale'),
('JOTR',  'Joshua Tree'),
('KATM',  'Katmai'),
('KEFJ',  'Kenai Fjords'),
('KICA',  'Kings Canyon'),
('KOVA',  'Kobuk Valley'),
('LACL',  'Lake Clark'),
('LAVO',  'Lassen Volcanic'),
('MACA',  'Mammoth Cave'),
('MEVE',  'Mesa Verde'),
('MORA',  'Mount Rainier'),
('NPSA',  'National Park of American Samoa'),
('NOCA',  'North Cascades'),
('OLYM',  'Olympic'),
('PEFO',  'Petrified Forest'),
('PINN',  'Pinnacles'),
('REDW',  'Redwood'),
('ROMO',  'Rocky Mountain'),
('SAGU',  'Saguaro'),
('SEKI',  'Sequoia and Kings Canyon National Parks'),
('SHEN',  'Shenandoah'),
('THRO',  'Theodore Roosevelt'),
('VIIS',  'Virgin Islands'),
('VOYA',  'Voyageurs'),
('WHSA',  'White Sands'),
('WICA',  'Wind Cave'),
('WRST',  'Wrangell-St Elias'),
('YELL',  'Yellowstone'),
('YOSE',  'Yosemite'),
('ZION',  'Zion');

insert into park_visit(start_date, end_date, user_id, park_id)
	values
('2020-02-15', '2020-02-20', 1, "YELL");