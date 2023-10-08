--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0
-- Dumped by pg_dump version 16.0

-- Started on 2023-10-08 01:16:42

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 4829 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 36352)
-- Name: admin; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.admin (
    admin_id character varying(255) NOT NULL,
    password character varying(100),
    username character varying(100)
);


ALTER TABLE public.admin OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 36357)
-- Name: merchant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.merchant (
    merchant_id character varying(255) NOT NULL,
    merchant_location text,
    merchant_name character varying(100),
    open boolean
);


ALTER TABLE public.merchant OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 36364)
-- Name: order_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_detail (
    order_detail_id character varying(255) NOT NULL,
    quantity integer,
    subtotal_price integer,
    order_id character varying(255) NOT NULL,
    product_id character varying(255) NOT NULL
);


ALTER TABLE public.order_detail OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 36371)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    order_id character varying(255) NOT NULL,
    completed boolean,
    destination_address text,
    order_time timestamp without time zone,
    total_price integer,
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 36378)
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    product_id character varying(255) NOT NULL,
    price integer,
    product_name character varying(255),
    merchant_id character varying(255) NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 36385)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id character varying(255) NOT NULL,
    email character varying(254),
    password character varying(100),
    username character varying(100)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 4818 (class 0 OID 36352)
-- Dependencies: 215
-- Data for Name: admin; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.admin (admin_id, password, username) FROM stdin;
42173b02-6145-11ee-8c99-0242ac120002	admin42min	admin1
bb6f4efe-6145-11ee-8c99-0242ac120002	admin24ad	admin2
\.


--
-- TOC entry 4819 (class 0 OID 36357)
-- Dependencies: 216
-- Data for Name: merchant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.merchant (merchant_id, merchant_location, merchant_name, open) FROM stdin;
1230ba00-282a-4376-91ea-ded5d818a6bf	Sleman	Gunkid	f
424ddfef-fe80-439e-bf66-2723a039eaf5	Jogja	GajahMungkur	f
48400dcc-ec36-4dc2-b0d3-3b8e6bb06ffe	Purwokerto	Mars	t
dbf6f8be-8f31-47b9-88fd-772ed3edfae0	Solo	CapKelapa	f
27a3ad6c-62d8-4923-b21c-aee56e020dd7	Kebumen	SambelPeyek	f
76ed0c91-cd54-43b9-83c3-01c6b186e7da	Solo	Sololagi	f
bcc052d6-4bf0-424e-822f-6fda26f1fbdf	GunungKidul	Meh	t
e96e98e9-69a7-40f2-97e9-d51aeeaba8dd	Semarang	DurenIjo	t
a448e884-8ca8-4816-8341-8e67f2cf4ddf	Bandung	Geger	t
11989545-a05c-49fa-8477-d254cd8dea26	Pekalongan	Bagonglar	f
db85b7d5-a69c-47d6-a699-5f4d3d3cdad3	Batam	CapSikil	f
3c970a72-8789-4430-8c1b-bcc3649cc60e	Jakarta	TujuhRatu	f
c5d8debf-e4c8-42d1-ac55-1d917707c9f1	Surabaya	GunungMabur	t
\.


--
-- TOC entry 4820 (class 0 OID 36364)
-- Dependencies: 217
-- Data for Name: order_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_detail (order_detail_id, quantity, subtotal_price, order_id, product_id) FROM stdin;
733d8f66-2a30-4e67-bcc5-f45a1b5724f4	1	14000	4aa432c0-0971-4475-a60c-75db881667c9	b28c246e-b8fb-4a43-8084-ffdfea9d89c1
50070b4e-3ea4-49d5-b0ac-bcda8a29d624	1	13000	4aa432c0-0971-4475-a60c-75db881667c9	9038334d-bcb4-47bb-b68a-5d3818cae584
d5440b0d-ade2-4030-827b-6a8d763858ef	1	3000	4aa432c0-0971-4475-a60c-75db881667c9	7dffefcb-b1e6-4b3a-af2d-75471430d01a
f7b6e9aa-b4eb-4850-b2ae-23580e84e8bc	1	6000	4aa432c0-0971-4475-a60c-75db881667c9	f11357d6-1d0b-4eec-92c2-eba887c17267
d39ca188-7b57-4888-ba72-4783c577454b	2	14000	4aa432c0-0971-4475-a60c-75db881667c9	40b6c6e0-b364-4b62-b48c-16cb35e49ee0
2957eba4-5b63-4fce-bcae-a8a2e22a9a31	2	26000	720b4ee1-a387-4edf-bc76-258457598d0e	9038334d-bcb4-47bb-b68a-5d3818cae584
149a2ec8-0fc5-4c09-89fb-26b2decc57a2	1	10000	720b4ee1-a387-4edf-bc76-258457598d0e	5da16f7f-10d3-4434-820e-334c024e38a0
aa49fabf-9544-4a51-94d2-d6662f4389c3	2	28000	aeb2e2c5-724d-441f-8ec3-dfcf268c9e42	b28c246e-b8fb-4a43-8084-ffdfea9d89c1
e86c8ffb-6b63-4ef8-927d-9c928df60e7d	2	20000	aeb2e2c5-724d-441f-8ec3-dfcf268c9e42	5da16f7f-10d3-4434-820e-334c024e38a0
0129d9a5-d8a3-4672-afc3-995c32f8c5ae	1	14000	1a0e7879-19c1-45f0-ab6a-56cfb5a81512	b28c246e-b8fb-4a43-8084-ffdfea9d89c1
6446a4f1-61b6-47f6-b47d-47c30a528248	2	6000	1a0e7879-19c1-45f0-ab6a-56cfb5a81512	7dffefcb-b1e6-4b3a-af2d-75471430d01a
\.


--
-- TOC entry 4821 (class 0 OID 36371)
-- Dependencies: 218
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (order_id, completed, destination_address, order_time, total_price, user_id) FROM stdin;
4aa432c0-0971-4475-a60c-75db881667c9	t	jonggol	2023-10-08 00:08:03.05	50000	9c42517a-e072-48ee-869c-d5c459820174
720b4ee1-a387-4edf-bc76-258457598d0e	t	jakarta	2023-10-08 00:17:01.025	36000	9c42517a-e072-48ee-869c-d5c459820174
aeb2e2c5-724d-441f-8ec3-dfcf268c9e42	t	jogja	2023-10-08 00:53:24.681	48000	9c42517a-e072-48ee-869c-d5c459820174
1a0e7879-19c1-45f0-ab6a-56cfb5a81512	t	sleman	2023-10-08 01:02:48.29	20000	9c42517a-e072-48ee-869c-d5c459820174
\.


--
-- TOC entry 4822 (class 0 OID 36378)
-- Dependencies: 219
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (product_id, price, product_name, merchant_id) FROM stdin;
b28c246e-b8fb-4a43-8084-ffdfea9d89c1	14000	NasiGoreng	76ed0c91-cd54-43b9-83c3-01c6b186e7da
5da16f7f-10d3-4434-820e-334c024e38a0	10000	SusuWedus	48400dcc-ec36-4dc2-b0d3-3b8e6bb06ffe
40b6c6e0-b364-4b62-b48c-16cb35e49ee0	7000	SemurLele	48400dcc-ec36-4dc2-b0d3-3b8e6bb06ffe
f11357d6-1d0b-4eec-92c2-eba887c17267	6000	MieGoreng	27a3ad6c-62d8-4923-b21c-aee56e020dd7
7dffefcb-b1e6-4b3a-af2d-75471430d01a	3000	TehAnget	db85b7d5-a69c-47d6-a699-5f4d3d3cdad3
e080a6a8-543c-44f0-a309-ce6358abb214	18000	BaksoJumbo	3c970a72-8789-4430-8c1b-bcc3649cc60e
9038334d-bcb4-47bb-b68a-5d3818cae584	13000	SateAyam	dbf6f8be-8f31-47b9-88fd-772ed3edfae0
\.


--
-- TOC entry 4823 (class 0 OID 36385)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, email, password, username) FROM stdin;
9c42517a-e072-48ee-869c-d5c459820174	najib@gmail.com	sauqi24	najib12
8b919f92-a10e-4f78-8fc1-c54d20a56473	sauqi@gmail.com	Rubba34	sauqi12
\.


--
-- TOC entry 4654 (class 2606 OID 36356)
-- Name: admin admin_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin
    ADD CONSTRAINT admin_pkey PRIMARY KEY (admin_id);


--
-- TOC entry 4658 (class 2606 OID 36363)
-- Name: merchant merchant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.merchant
    ADD CONSTRAINT merchant_pkey PRIMARY KEY (merchant_id);


--
-- TOC entry 4662 (class 2606 OID 36370)
-- Name: order_detail order_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT order_detail_pkey PRIMARY KEY (order_detail_id);


--
-- TOC entry 4664 (class 2606 OID 36377)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (order_id);


--
-- TOC entry 4666 (class 2606 OID 36384)
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (product_id);


--
-- TOC entry 4660 (class 2606 OID 36395)
-- Name: merchant uk9d99mmka6ug1kl85hatmdji3m; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.merchant
    ADD CONSTRAINT uk9d99mmka6ug1kl85hatmdji3m UNIQUE (merchant_name);


--
-- TOC entry 4656 (class 2606 OID 36393)
-- Name: admin ukgfn44sntic2k93auag97juyij; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.admin
    ADD CONSTRAINT ukgfn44sntic2k93auag97juyij UNIQUE (username);


--
-- TOC entry 4668 (class 2606 OID 36397)
-- Name: users ukr43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 4670 (class 2606 OID 36391)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4673 (class 2606 OID 36408)
-- Name: orders fk32ql8ubntj5uh44ph9659tiih; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk32ql8ubntj5uh44ph9659tiih FOREIGN KEY (user_id) REFERENCES public.users(user_id);


--
-- TOC entry 4671 (class 2606 OID 36403)
-- Name: order_detail fkb8bg2bkty0oksa3wiq5mp5qnc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT fkb8bg2bkty0oksa3wiq5mp5qnc FOREIGN KEY (product_id) REFERENCES public.product(product_id);


--
-- TOC entry 4674 (class 2606 OID 36413)
-- Name: product fkk47qmalv2pg906heielteubu7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT fkk47qmalv2pg906heielteubu7 FOREIGN KEY (merchant_id) REFERENCES public.merchant(merchant_id);


--
-- TOC entry 4672 (class 2606 OID 36398)
-- Name: order_detail fkrws2q0si6oyd6il8gqe2aennc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_detail
    ADD CONSTRAINT fkrws2q0si6oyd6il8gqe2aennc FOREIGN KEY (order_id) REFERENCES public.orders(order_id);


-- Completed on 2023-10-08 01:16:43

--
-- PostgreSQL database dump complete
--

