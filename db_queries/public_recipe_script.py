import sys
sys.stdout.reconfigure(encoding='utf-8')

# 라이브러리 먼저 설치해주세요
from sqlalchemy import create_engine, Column, String, Integer, Text, ForeignKey
from sqlalchemy.orm import declarative_base, sessionmaker
import requests

# 데이터베이스 설정 (mysqlclient 사용) 본인 루트비번 입력하시면 됩니다.
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
    __tablename__ = 'recipe_manual'  # 테이블 이름
    
    recipe_manual_id = Column(Integer, primary_key=True, autoincrement=True)
    recipe_manual_step = Column(Integer, nullable=False)  # 메뉴얼 단계
    recipe_manual_image = Column(Text)  # 메뉴얼 이미지
    recipe_manual_content = Column(Text, nullable=False)  # 메뉴얼 내용
    recipe_id = Column(Integer, ForeignKey('recipe.recipe_id'), nullable=False)  # 레시피 외래키

# 레시피 이름으로 검색
def find_recipe_by_name(menu_name):
    return session.query(Recipe).filter_by(menu_name=menu_name).first()

# 데이터 삽입 부분 수정 (한 번에 데이터 추가 후 commit)
def insert_data(data):
    for item in data:
        if item['RCP_PAT2'] != '후식':  # '후식' 제외
            # 레시피가 존재하는지 확인
            existing_recipe = find_recipe_by_name(item['RCP_NM'])
            if not existing_recipe:
                # recipe 테이블에 삽입
                recipe = Recipe(
                    menu_name=item['RCP_NM'],
                    menu_ingredient=item['RCP_PARTS_DTLS'],
                    menu_image=item['ATT_FILE_NO_MAIN'],
                    user_id=1
                )
                session.add(recipe)
                session.commit()  # commit을 통해 recipe_id를 생성

                # public_data_recipe 테이블에 삽입
                public_data_recipe = PublicDataRecipe(
                    public_data_menu_name=item['RCP_NM'],
                    public_data_menu_ingredient=item['RCP_PARTS_DTLS'],
                    public_data_menu_image=item['ATT_FILE_NO_MAIN'],
                    recipe_id=recipe.recipe_id  # commit 후 생성된 recipe_id 사용
                )
                session.add(public_data_recipe)

                # recipe_manual 테이블에 메뉴얼 데이터 삽입
                for i in range(1, 21):
                    manual_key = f'MANUAL{i:02}'
                    manual_img_key = f'MANUAL_IMG{i:02}'
                    if item[manual_key]:
                        recipe_manual = RecipeManual(
                            recipe_manual_step=i,  # 단계 추가
                            recipe_manual_content=item[manual_key],
                            recipe_manual_image=item.get(manual_img_key, None),
                            recipe_id=recipe.recipe_id  # commit 후 생성된 recipe_id 사용
                        )
                        session.add(recipe_manual)
            else:
                print(f"레시피 '{item['RCP_NM']}'가 이미 존재합니다. 삽입하지 않습니다.")
    
    # 모든 데이터 추가 후 한 번에 commit
    session.commit()

# API에서 데이터를 가져오는 함수 (범위 설정 추가)
def fetch_data(start, end):
    url = f"https://openapi.foodsafetykorea.go.kr/api/838af5d280254da4acbb/COOKRCP01/json/{start}/{end}"
    response = requests.get(url)
    data = response.json()
    return data['COOKRCP01']['row']

# 데이터 삽입 실행
try:
    # 첫 번째 요청: 1~1000
    data1 = fetch_data(1, 1000)
    insert_data(data1)

    # 두 번째 요청: 1001~1124
    data2 = fetch_data(1001, 1124)
    insert_data(data2)

finally:
    session.close()
