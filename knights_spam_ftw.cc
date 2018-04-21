#include <iostream>
#include <string>
#include <algorithm>
#include <vector>
#include <unordered_map>
#include <cmath>

using namespace std;

class Site
{
    public:
        Site(int siteId, int x, int y, int radius)
            : m_siteId(siteId)
            , m_x(x)
            , m_y(y)
            , m_radius(radius)
        { }

        int x() const
        {
            return m_x;
        }

        int y() const
        {
            return m_y;
        }

        int getRadius() const
        {
            return m_radius;
        }

        int getStructureType() const
        {
            return m_structureType;
        }

        void setOwner(int owner)
        {
            m_owner = owner;
        }

        void setStructureType(int structureType)
        {
            m_structureType = structureType;
        }

    private:
        int m_siteId;
        int m_x;
        int m_y;
        int m_radius;
        int m_owner;
        int m_structureType;
};

class Unit
{
    public:
        Unit(int x, int y, int owner, int unitType, int health)
            : m_x(x)
            , m_y(y)
            , m_owner(owner)
            , m_unitType(unitType)
            , m_health(health)
        { }

        int x() const
        {
            return m_x;
        }

        int y() const
        {
            return m_y;
        }

        bool isQueen() const
        {
            return (m_unitType == -1);
        }

        bool isFriendly() const
        {
            return m_owner == 0;
        }

    public:
        int m_x;
        int m_y;
        int m_owner;
        int m_unitType;
        int m_health;
};

class Game
{
    public:
        Game() = default;

        void init()
        {
            cin >> m_numSites;
            cin.ignore();

            int siteId;
            int x;
            int y;
            int radius;

            for(int i = 0; i < m_numSites; ++i)
            {
                cin >> siteId >> x >> y >> radius;
                cin.ignore();

                Site site(siteId, x, y, radius);
                std::pair<int, Site> pair(siteId, site);
                m_sites.insert(pair);
            }

            m_currentSite = -1;
        }

        void updateSites()
        {
            int siteId;
            int ignore1; // used in future leagues
            int ignore2; // used in future leagues
            int structureType; // -1 = No structure, 2 = Barracks
            int sOwner; // -1 = No structure, 0 = Friendly, 1 = Enemy
            int param1;
            int param2;

            for(int i = 0; i < m_numSites; ++i)
            {
                cin >> siteId >> ignore1 >> ignore2 >> structureType >> sOwner >> param1 >> param2;
                cin.ignore();

                auto s = m_sites.find(siteId);
                if(s != m_sites.end())
                {
                    s->second.setStructureType(structureType);
                    s->second.setOwner(sOwner);
                }
            }
        }

        void updateUnits()
        {
            int numUnits;
            cin >> numUnits;
            cin.ignore();

            int x;
            int y;
            int uOwner;
            int unitType; // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
            int health;

            for(int i = 0; i < numUnits; i++)
            {
                cin >> x >> y >> uOwner >> unitType >> health;
                cin.ignore();

                Unit u(x, y, uOwner, unitType, health);
                m_units.push_back(u);
            }
        }

        void gameLoop()
        {
            m_units.clear();

            int gold;
            int touchedSite; // -1 if none
            cin >> gold >> touchedSite;
            cin.ignore();

            updateSites();
            updateUnits();

            const Unit* queen = findQueen();

            int closestSite = findClosestSite();
            auto site = m_sites.find(closestSite);

            if(vecLen(queen->x(), queen->y(), site->second.x(), site->second.y()) - 33 > site->second.getRadius())
            {
                cout << "MOVE " << site->second.x() << " " << site->second.y() << endl;
                cout << "TRAIN" << endl;
            }
            else if(site->second.getStructureType() == -1)
            {
                m_currentSite = closestSite;
                cout << "BUILD " << m_currentSite << " BARRACKS-KNIGHT" << endl;
                cout << "TRAIN " << m_currentSite << endl;
            }
            else
            {
                cout << "WAIT" << endl;
                cout << "TRAIN " << m_currentSite << endl;
            }
        }

    private:
        float vecLen(int x1, int y1, int x2, int y2)
        {
            int deltaX = x1 - x2;
            int deltaY = y1 - y2;

            return sqrt(pow(deltaX, 2) + pow(deltaY, 2));
        }

        const Unit* findQueen()
        {
            for(const Unit& u : m_units)
            {
                if(u.isQueen() && u.isFriendly())
                {
                    return &u;
                }
            }

            return nullptr;
        }

        int findClosestSite()
        {
            const Unit* queen = findQueen();

            float minLen = 9999999;
            int closestSite = -1;

            for(auto& s : m_sites)
            {
                if(minLen > vecLen(queen->x(), queen->y(), s.second.x(), s.second.y()))
                {
                    closestSite = s.first;
                    minLen = vecLen(queen->x(), queen->y(), s.second.x(), s.second.y());
                }
            }

            return closestSite;
        }

    private:
        std::unordered_map<int, Site> m_sites;
        std::vector<Unit> m_units;
        int m_numSites;
        int m_currentSite;
};

int
main()
{
    Game game;

    game.init();

    while(1)
    {
        game.gameLoop();
    }
}
