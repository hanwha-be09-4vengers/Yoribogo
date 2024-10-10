import sys
sys.stdout.reconfigure(encoding='utf-8')  # 한글 출력을 위해 추가

# 라이브러리 설치 후 진행해주세요
import requests
from sqlalchemy import create_engine, Column, String, Integer, Text, ForeignKey
from sqlalchemy.orm import declarative_base, sessionmaker

# 데이터베이스 설정, 본인 계정과 비번 입력하세요
DATABASE_URL = 'mysql+mysqldb://root:mariadb@localhost/yoribogodb'

engine = create_engine(DATABASE_URL)
Session = sessionmaker(bind=engine)
session = Session()

Base = declarative_base()

# 테이블 정의
class Recipe(Base):
    __tablename__ = 'recipe'
    
    recipe_id = Column(Integer, primary_key=True, autoincrement=True)
    menu_name = Column(String(255), nullable=False)
    menu_ingredient = Column(Text, nullable=False)
    menu_image = Column(Text)
    menu_type = Column(String(255), default='PUBLIC')
    user_id = Column(Integer, nullable=False)

class PublicDataRecipe(Base):
    __tablename__ = 'public_data_recipe'
    
    public_data_recipe_id = Column(Integer, primary_key=True, autoincrement=True)
    public_data_menu_name = Column(String(255), nullable=False)
    public_data_menu_ingredient = Column(Text, nullable=False)
    public_data_menu_image = Column(Text)
    recipe_id = Column(Integer, ForeignKey('recipe.recipe_id'), nullable=False)

class RecipeManual(Base):
    __tablename__ = 'recipe_manual'
    
    recipe_manual_id = Column(Integer, primary_key=True, autoincrement=True)
    recipe_manual_step = Column(Integer, nullable=False)  # 단계 추가
    manual_content = Column(Text, nullable=False)
    manual_menu_image = Column(Text)
    recipe_id = Column(Integer, ForeignKey('recipe.recipe_id'), nullable=False)

# 레시피가 이미 있는지 확인하는 함수
def find_recipe_by_name(menu_name):
    return session.query(Recipe).filter_by(menu_name=menu_name).first()

# 데이터 삽입 함수
def insert_data(data):
    for item in data:
        if item['RCP_PAT2'] != '후식':  # '후식' 제외
            existing_recipe = find_recipe_by_name(item['RCP_NM'])
            if not existing_recipe:
                recipe = Recipe(
                    menu_name=item['RCP_NM'],
                    menu_ingredient=item['RCP_PARTS_DTLS'],
                    menu_image=item['ATT_FILE_NO_MAIN'],
                    user_id=1
                )
                session.add(recipe)
                session.commit()

                public_data_recipe = PublicDataRecipe(
                    public_data_menu_name=item['RCP_NM'],
                    public_data_menu_ingredient=item['RCP_PARTS_DTLS'],
                    public_data_menu_image=item['ATT_FILE_NO_MAIN'],
                    recipe_id=recipe.recipe_id
                )
                session.add(public_data_recipe)

                for i in range(1, 21):
                    manual_key = f'MANUAL{i:02}'
                    manual_img_key = f'MANUAL_IMG{i:02}'
                    if item[manual_key]:
                        recipe_manual = RecipeManual(
                            recipe_manual_step=i,  # 단계 추가
                            manual_content=item[manual_key],
                            manual_menu_image=item[manual_img_key],
                            recipe_id=recipe.recipe_id
                        )
                        session.add(recipe_manual)
                session.commit()
            else:
                print(f"레시피 '{item['RCP_NM']}'가 이미 존재합니다. 삽입하지 않습니다.")

# API로 데이터 가져오기
def fetch_data():
    API_URL_1 = 'https://openapi.foodsafetykorea.go.kr/api/838af5d280254da4acbb/COOKRCP01/json/1/1000'
    API_URL_2 = 'https://openapi.foodsafetykorea.go.kr/api/838af5d280254da4acbb/COOKRCP01/json/1001/1124'

    response_1 = requests.get(API_URL_1)
    response_2 = requests.get(API_URL_2)
    
    if response_1.status_code == 200:
        data1 = response_1.json().get('COOKRCP01', {}).get('row', [])
        print(f"첫 번째 데이터 수: {len(data1)}")
        insert_data(data1)
    else:
        print(f"첫 번째 API 호출 실패: {response_1.status_code}")

    if response_2.status_code == 200:
        data2 = response_2.json().get('COOKRCP01', {}).get('row', [])
        print(f"두 번째 데이터 수: {len(data2)}")
        insert_data(data2)
    else:
        print(f"두 번째 API 호출 실패: {response_2.status_code}")

# 데이터 가져오기 실행
fetch_data()
