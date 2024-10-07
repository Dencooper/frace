import { Card, Col, Row, Statistic } from "antd";
import { useEffect, useState } from 'react';
import { Result } from "antd";


import CountUp from 'react-countup';
import { useAppSelector } from '@/redux/hooks';
import { callCountUser } from 'config/api';
import { ALL_PERMISSIONS } from '@/config/permissions';


const DashboardPage = () => {
    const permissions = useAppSelector(state => state.account.user.role.permissions);
    const isAdmin = permissions?.find(item =>
        item.apiPath === ALL_PERMISSIONS.PERMISSIONS.GET_PAGINATE.apiPath
        && item.method === ALL_PERMISSIONS.USERS.GET_PAGINATE.method
    ) ? true : false;

    const formatter = (value: number | string) => {
        return (
            <CountUp end={Number(value)} separator="," />
        );
    };

    const [quantityUser, setQuantityUser] = useState();
    const [quantityCompany, setQuantityCompany] = useState();
    const [quantityJob, setQuantityJob] = useState();

    const init = async () => {
        const res = await callCountUser();
        setQuantityUser(res.data[0]);
        setQuantityCompany(res.data[1]);
        setQuantityJob(res.data[2]);

    }
    useEffect(() => {
        init();
    }, [])

    return (
        <Row gutter={[20, 20]}>
            {isAdmin &&
                <Col span={24} md={8}>
                    <Card title="User" bordered={false} >
                        <Statistic
                            title="Active Users"
                            value={quantityUser}
                            formatter={formatter}
                        />

                    </Card>
                </Col>
            }
            {permissions?.length == 0 ?
                <Col span={24} md={24}>
                    <Result
                        status="403"
                        title="Truy cập bị từ chối"
                        subTitle="Xin lỗi, bạn không có quyền hạn (permission) truy cập thông tin này"
                    />
                </Col>

                :
                <>
                    <Col span={24} md={8}>
                        <Card title="Company" bordered={false} >
                            <Statistic
                                title="Active Comanies"
                                value={quantityCompany}
                                formatter={formatter}
                            />
                        </Card>
                    </Col>
                    <Col span={24} md={8}>
                        <Card title="Job" bordered={false} >
                            <Statistic
                                title="Active Jobs"
                                value={quantityJob}
                                formatter={formatter}
                            />
                        </Card>
                    </Col>
                </>

            }
        </Row>


    )
}

export default DashboardPage;