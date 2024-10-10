import sys
sys.stdout.reconfigure(encoding='utf-8')

# pip install 로 라이브러리 먼저 설치해주세요
from sqlalchemy import create_engine, Column, String, Integer, Text, ForeignKey
from sqlalchemy.orm import declarative_base, sessionmaker, relationship
import requests

# 데이터베이스 설정 (pymysql 없이 연결)
DATABASE_URL = 'mysql+mysqldb://root:mariadb@localhost/yoribogodb'

engine = create_engine(DATABASE_URL)
Session = sessionmaker(bind=engine)
session = Session()

Base = declarative_base()

# 테이블 모델 정의
class Recipe(Base):
    __tablename__ = 'recipe'  # 이미 구축된 테이블 이름
    
    recipe_id = Column(Integer, primary_key=True, autoincrement=True)
    menu_name = Column(String(255), nullable=False)
    menu_ingredient = Column(Text, nullable=False)
    menu_image = Column(Text)
    menu_type = Column(String(255), default='PUBLIC')
    recipe_status = Column(String(255), default='ACTIVE')
    user_id = Column(Integer, default=1)  # user_id에 기본값 1 설정

class PublicDataRecipe(Base):
    __tablename__ = 'public_data_recipe'  # 이미 구축된 테이블 이름
    
    public_data_recipe_id = Column(Integer, primary_key=True, autoincrement=True)
    public_data_menu_name = Column(String(255), nullable=False)
    public_data_menu_ingredient = Column(Text, nullable=False)
    public_data_menu_image = Column(Text)
    recipe_id = Column(Integer, ForeignKey('recipe.recipe_id'), nullable=False)

class RecipeManual(Base):
    __tablename__ = 'recipe_manual'  # 이미 구축된 테이블 이름
    
    recipe_manual_id = Column(Integer, primary_key=True, autoincrement=True)
    manual_menu_image = Column(Text)
    manual_content = Column(Text, nullable=False)
    recipe_id = Column(Integer, ForeignKey('recipe.recipe_id'), nullable=False)

# API 호출
def fetch_data(start, end):
    url = f"https://openapi.foodsafetykorea.go.kr/api/838af5d280254da4acbb/COOKRCP01/json/{start}/{end}"
    response = requests.get(url)
    data = response.json()
    return data['COOKRCP01']['row']

# 레시피 이름으로 검색
def find_recipe_by_name(menu_name):
    return session.query(Recipe).filter_by(menu_name=menu_name).first()

# 데이터 삽입 함수
def insert_data(data):
    for item in data:
        if item['RCP_PAT2'] != '후식':
            # 레시피 이름으로 먼저 검색
            existing_recipe = find_recipe_by_name(item['RCP_NM'])
            if not existing_recipe:
                # recipe 테이블에 삽입
                recipe = Recipe(
                    menu_name=item['RCP_NM'],
                    menu_ingredient=item['RCP_PARTS_DTLS'],
                    menu_image=item['ATT_FILE_NO_MAIN'],
                    user_id=1  # user_id 기본값 1로 설정
                )
                session.add(recipe)
                session.commit()

                # public_data_recipe 테이블에 삽입
                public_data_recipe = PublicDataRecipe(
                    public_data_menu_name=item['RCP_NM'],
                    public_data_menu_ingredient=item['RCP_PARTS_DTLS'],
                    public_data_menu_image=item['ATT_FILE_NO_MAIN'],
                    recipe_id=recipe.recipe_id
                )
                session.add(public_data_recipe)
                session.commit()

                # recipe_manual 테이블에 메뉴얼 데이터 삽입
                for i in range(1, 21):
                    manual_key = f'MANUAL{i:02}'
                    manual_img_key = f'MANUAL_IMG{i:02}'
                    if item[manual_key]:
                        recipe_manual = RecipeManual(
                            manual_content=item[manual_key],
                            manual_menu_image=item[manual_img_key],
                            recipe_id=recipe.recipe_id
                        )
                        session.add(recipe_manual)
                session.commit()
            else:
                print(f"레시피 '{item['RCP_NM']}'가 이미 존재합니다. 삽입하지 않습니다.")

# 데이터 가져오기 및 저장
try:
    # 첫 번째 요청: 1~1000
    data1 = fetch_data(1, 1000)
    insert_data(data1)
    
    # 두 번째 요청: 1001~1124
    data2 = fetch_data(1001, 1124)
    insert_data(data2)

finally:
    session.close()
